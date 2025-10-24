package com.hamza.job_tracker.repository;




import com.hamza.job_tracker.dto.StatDTO;
import com.hamza.job_tracker.entity.Job;
import com.hamza.job_tracker.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findTop2ByOrderByDateCandidatureDesc();


    List<Job> findByDateReponseAfter(LocalDate dateLimit);
    long countByStatus(JobStatus status);

    long countByStatusAndDateCandidatureBefore(JobStatus status, LocalDate date);

    @Query("SELECT new com.hamza.job_tracker.dto.StatDTO(j.siteRecommandation, COUNT(j)) FROM Job j WHERE j.siteRecommandation IS NOT NULL GROUP BY j.siteRecommandation")
    List<StatDTO> countJobsBySite();

    @Query("SELECT new com.hamza.job_tracker.dto.StatDTO(j.entreprise.ville, COUNT(j)) FROM Job j WHERE j.entreprise.ville IS NOT NULL GROUP BY j.entreprise.ville")
    List<StatDTO> countJobsByCity();

    List<Job> findByDateExpirationBetween(LocalDate start, LocalDate end);

    List<Job> findByIsFavoriteTrueOrderByDateCandidatureDesc();
}