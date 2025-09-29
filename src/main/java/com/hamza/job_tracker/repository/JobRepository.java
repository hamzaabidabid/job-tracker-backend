package com.hamza.job_tracker.repository;




import com.hamza.job_tracker.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findTop5ByOrderByDateCandidatureDesc();


    List<Job> findByDateReponseAfter(LocalDate dateLimit);


    List<Job> findByDateExpirationBetween(LocalDate start, LocalDate end);
}