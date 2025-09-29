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
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Ex: "Java", "Angular", "SQL"
    @ManyToMany(mappedBy = "requiredSkills")
    @ToString.Exclude // Empêche la boucle dans le toString()
    @JsonIgnore // Ignore ce champ lors de la sérialisation JSON pour casser la boucle
    private Set<Job> jobs;
}