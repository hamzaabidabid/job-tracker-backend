package com.hamza.job_tracker.controller;

import com.hamza.job_tracker.dto.DashboardStatsDTO;
import com.hamza.job_tracker.dto.StatDTO;
import com.hamza.job_tracker.entity.Job;
import com.hamza.job_tracker.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs") // Préfixe commun pour toutes les routes de ce contrôleur
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080","http://127.0.0.1"})

public class JobRestController {

    @Autowired
    private JobService jobService;

    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        return job != null ? ResponseEntity.ok(job) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Job createJob(@RequestBody Job job) {
        return jobService.saveJob(job);
    }

    @PutMapping("/{id}")
    public Job updateJob(@PathVariable Long id, @RequestBody Job jobDetails) {
        // Logique pour s'assurer que l'ID est correct avant de sauvegarder
        jobDetails.setId(id);
        return jobService.saveJob(jobDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/favorites")
    public List<Job> getFavoriteJobs() {
        return jobService.getFavoriteJobs();
    }

    @PatchMapping("/{id}/favorite") // PATCH est sémantiquement correct pour une mise à jour partielle
    public ResponseEntity<Job> toggleFavorite(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.toggleFavoriteStatus(id));
    }



    @GetMapping("/recent-responses")
    public List<Job> getJobsWithRecentResponse() {
        return jobService.getJobsWithRecentResponse(3); // réponse dans les 3 jours
    }

    @GetMapping("/expiring-soon")
    public List<Job> getExpiringJobs() {
        return jobService.getExpiringJobs(10); // expirant dans les 10 jours
    }

    @GetMapping("/stats/by-city")
    public List<StatDTO> getJobsByCity() {
        return jobService.countJobsByCity();
    }
    @GetMapping("/latest")
    public List<Job> getLatestJobs() {
        return jobService.getLatestJobs();
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        return ResponseEntity.ok(jobService.getDashboardStats());
    }
}