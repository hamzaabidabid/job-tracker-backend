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

    // --- CORRECTION 3 : Rendre ces champs optionnels pour éviter les erreurs ---
    @Column(nullable = true) // <-- METTRE 'true' AU LIEU DE 'false'
    private String telephone;

    @Column(nullable = true) // <-- METTRE 'true' AU LIEU DE 'false'
    private String email;


    @OneToMany(mappedBy = "entreprise", fetch = FetchType.LAZY) // LAZY est souvent mieux ici
    @ToString.Exclude // Empêche la boucle dans le toString()
    @JsonIgnore // Ignore ce champ lors de la sérialisation JSON pour casser la boucle
    private Set<Job> jobs;
}