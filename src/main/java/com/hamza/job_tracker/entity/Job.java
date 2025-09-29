package com.hamza.job_tracker.entity;

import com.hamza.job_tracker.enums.JobStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDate dateLancement;
    private LocalDate dateCandidature;
    private LocalDate dateReponse;
    private LocalDate dateExpiration;
    private String siteRecommandation;
    private String urlOffre;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    // --- CORRECTION 1 : AJOUTER LA CASCADE ICI ---
    @ManyToOne(cascade = CascadeType.ALL) // <-- AJOUTEZ CECI
    @JoinColumn(name = "entreprise_id", nullable = false)
    private Entreprise entreprise;

    // --- CORRECTION 2 : AJOUTER LA CASCADE ICI ---
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) // <-- AJOUTEZ CECI
    @JoinTable(
            name = "job_skill",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @ToString.Exclude // Empêche la boucle dans le toString()
    private Set<Skill> requiredSkills;
}