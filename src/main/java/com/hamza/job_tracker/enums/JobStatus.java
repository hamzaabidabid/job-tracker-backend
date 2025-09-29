package com.hamza.job_tracker.enums;

public enum JobStatus {
    EN_COURS("En cours"),
    REFUSE("Refusé"),
    ACCEPTE("Accepté"),
    ARCHIVE("Archivé");

    private final String displayName;

    JobStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}