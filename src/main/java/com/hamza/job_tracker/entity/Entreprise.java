package com.hamza.job_tracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Entreprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String secteur;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String adresse;
    private String ville;

    @Column(nullable = true)
    private String telephone;

    @Column(nullable = true)
    private String email;

    @OneToMany(mappedBy = "entreprise", fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<Job> jobs;

    // SUPPRESSION du getter manuel. Lombok s'en occupe.
}