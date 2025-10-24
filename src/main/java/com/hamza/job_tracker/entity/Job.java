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

    // Utiliser une cascade plus sûre que .ALL
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "entreprise_id", nullable = false)
    private Entreprise entreprise;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "job_skill",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @ToString.Exclude
    private Set<Skill> requiredSkills;

    private boolean isFavorite = false;
    // SUPPRESSION des getters/setters manuels. Lombok s'en occupe.
}