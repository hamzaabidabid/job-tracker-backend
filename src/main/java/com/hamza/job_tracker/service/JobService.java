package com.hamza.job_tracker.service;

import com.hamza.job_tracker.dto.DashboardStatsDTO;
import com.hamza.job_tracker.dto.StatDTO;
import com.hamza.job_tracker.entity.Entreprise;
import com.hamza.job_tracker.entity.Job;
import com.hamza.job_tracker.enums.JobStatus;
import com.hamza.job_tracker.repository.EntrepriseRepository;
import com.hamza.job_tracker.repository.JobRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JobService {
   @Autowired
    private  JobRepository jobRepository;
    @Autowired
    private  EntrepriseRepository entrepriseRepository;

    // Le constructeur demande maintenant DEUX arguments


    // 2. Créer un constructeur pour l'injection (plus d' @Autowired sur les champs)
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Transactional // Important pour gérer les entités dans la même transaction
    public Job saveJob(Job job) {
        // 1. Gérer l'entité Entreprise
        Entreprise entreprise = job.getEntreprise();
        if (entreprise != null) {
            // CAS 1 : C'est une entreprise existante (elle a un ID)
            if (entreprise.getId() != null && entrepriseRepository.existsById(entreprise.getId())) {
                // On récupère l'entité managée depuis la base de données
                Entreprise managedEntreprise = entrepriseRepository.findById(entreprise.getId()).get();
                // On attache cette entité managée au job
                job.setEntreprise(managedEntreprise);
            }
            // CAS 2 : C'est une nouvelle entreprise (pas d'ID)
            // Hibernate la sauvegardera grâce à la cascade, pas besoin de code ici.
        }

        // 2. Gérer les compétences (s'assurer qu'elles ne sont pas détachées)
        // Cette logique est plus complexe, pour l'instant la cascade devrait suffire
        // si les compétences sont toujours nouvelles.

        // 3. Sauvegarder le Job
        // La cascade s'occupera de persister la nouvelle entreprise si nécessaire.
        return jobRepository.save(job);
    }
    public DashboardStatsDTO getDashboardStats() {
        long totalApplications = jobRepository.count();
        long refusedApplications = jobRepository.countByStatus(JobStatus.REFUSE);

        // Candidatures "en cours" de plus de 7 jours
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        long pendingApplications = jobRepository.countByStatusAndDateCandidatureBefore(JobStatus.EN_COURS, sevenDaysAgo);

        List<StatDTO> applicationsByCity = jobRepository.countJobsByCity();
        List<StatDTO> applicationsBySite = jobRepository.countJobsBySite();

        return new DashboardStatsDTO(
                totalApplications,
                refusedApplications,
                pendingApplications,
                applicationsByCity,
                applicationsBySite
        );
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }
    public List<Job> getFavoriteJobs() {
        return jobRepository.findByIsFavoriteTrueOrderByDateCandidatureDesc();
    }

    // NOUVELLE MÉTHODE POUR BASCULER LE STATUT DE FAVORI
    @Transactional
    public Job toggleFavoriteStatus(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with id: " + id));

        job.setFavorite(!job.isFavorite()); // Inverse la valeur actuelle

        return jobRepository.save(job);
    }
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    // --- NOUVEAUX SERVICES POUR LE DASHBOARD ---

    // 3. Adapter l'appel à la nouvelle méthode du repository
    public List<Job> getLatestJobs() {
        // Cette méthode récupère maintenant les 5 derniers jobs
        return jobRepository.findTop2ByOrderByDateCandidatureDesc();
    }

    public List<Job> getJobsWithRecentResponse(int days) {
        LocalDate dateLimit = LocalDate.now().minusDays(days); // Ex: aujourd'hui - 3 jours
        return jobRepository.findByDateReponseAfter(dateLimit); // Correct
    }

    public List<Job> getExpiringJobs(int days) {
        LocalDate today = LocalDate.now();
        LocalDate expirationLimit = today.plusDays(days); // Ex: aujourd'hui + 10 jours
        return jobRepository.findByDateExpirationBetween(today, expirationLimit); // Correct
    }

    public List<StatDTO> countJobsByCity() {
        return jobRepository.findAll().stream()
                .filter(job -> job.getEntreprise() != null && job.getEntreprise().getVille() != null && !job.getEntreprise().getVille().isEmpty())
                .collect(Collectors.groupingBy(job -> job.getEntreprise().getVille(), Collectors.counting()))
                .entrySet().stream()
                .map(entry -> new StatDTO(String.valueOf(entry.getKey()), entry.getValue()))
                // Transformation de la Map en List<StatDTO>
                .collect(Collectors.toList());
    }
}