package com.hamza.job_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private long totalApplications;
    private long refusedApplications;
    private long pendingApplications; // Candidatures de +7 jours sans réponse
    private List<StatDTO> applicationsByCity;
    private List<StatDTO> applicationsBySite;
}