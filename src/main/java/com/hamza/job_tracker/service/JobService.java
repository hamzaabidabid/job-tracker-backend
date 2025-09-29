package com.hamza.job_tracker.service;

import com.hamza.job_tracker.dto.StatDTO;
import com.hamza.job_tracker.entity.Job;
import com.hamza.job_tracker.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JobService {

    // 1. Déclarer les dépendances en 'final'
    private final JobRepository jobRepository;

    // 2. Créer un constructeur pour l'injection (plus d' @Autowired sur les champs)
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    // --- NOUVEAUX SERVICES POUR LE DASHBOARD ---

    // 3. Adapter l'appel à la nouvelle méthode du repository
    public List<Job> getLatestJobs() {
        // Cette méthode récupère maintenant les 5 derniers jobs
        return jobRepository.findTop5ByOrderByDateCandidatureDesc();
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
                .map(entry -> new StatDTO(entry.getKey(), entry.getValue())) // Transformation de la Map en List<StatDTO>
                .collect(Collectors.toList());
    }
}