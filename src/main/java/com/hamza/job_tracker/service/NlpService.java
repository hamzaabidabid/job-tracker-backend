package com.hamza.job_tracker.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NlpService {

    // Notre "dictionnaire" de compétences. On peut l'enrichir à l'infini.
    // Utiliser un Set pour des recherches rapides et éviter les doublons.
    private static final Set<String> SKILL_DICTIONARY = new HashSet<>(Arrays.asList(
            // Langages & Frameworks Backend
            "JAVA", "SPRING BOOT", "NODE.JS", "EXPRESS", "NEST", "PYTHON", "FASTAPI", "DJANGO",
            "PHP", "LARAVEL", "SYMFONY", "GO", "GOLANG", "RUBY", "RAILS", ".NET", "C#",
            // Langages & Frameworks Frontend
            "REACT", "NEXT.JS", "ANGULAR", "VUE.JS", "TYPESCRIPT", "JAVASCRIPT", "HTML", "CSS", "SASS",
            // Mobile
            "FLUTTER", "REACT NATIVE", "SWIFT", "KOTLIN", "ANDROID", "IOS",
            // Bases de données
            "SQL", "NOSQL", "MYSQL", "POSTGRESQL", "MONGODB", "REDIS", "ORACLE", "SQL SERVER",
            // Ops & DevOps
            "GIT", "DOCKER", "KUBERNETES", "K8S", "CI/CD", "GITHUB ACTIONS", "GITLAB CI", "JENKINS",
            "TERRAFORM", "ANSIBLE", "AWS", "GCP", "AZURE", "CLOUD",
            // Monitoring & Observability
            "SENTRY", "PROMETHEUS", "GRAFANA", "DATADOG", "ELK", "LOGS", "ALERTING",
            // Architecture & Concepts
            "API", "RESTFUL", "GRAPHQL", "WEBSOCKETS", "EVENT-DRIVEN", "MICROSERVICES", "SOLID",
            "AUTHN", "AUTHZ", "CHIFFREMENT", "OWASP", "RGPD", "SÉCURITÉ", "TESTS UNITAIRES", "TESTS D'INTÉGRATION",
            // Soft Skills & Méthodologies
            "AGILE", "SCRUM", "MENTORING", "REVUES DE CODE", "UX", "UI", "FEATURE FLAGS", "ANGLAIS TECHNIQUE"
    ));


    public Set<String> extractSkills(String text) {
        if (text == null || text.isEmpty()) {
            return new HashSet<>();
        }

        // Étape 1 : Normaliser le texte de l'offre en minuscules.
        // Si le texte contient "Java" ou "JAVA", il devient "java".
        String normalizedText = text.toLowerCase();

        // Étape 2 : Parcourir le dictionnaire de compétences (qui est en MAJUSCULES).
        return SKILL_DICTIONARY.stream()
                // Étape 3 : Filtrer
                .filter(skillFromDictionary -> // ex: skillFromDictionary = "JAVA"
                        // On compare la version minuscule du mot du dictionnaire ("java")
                        // avec le texte normalisé de l'offre ("...java...")
                        normalizedText.contains(skillFromDictionary.toLowerCase())
                )
                // Étape 4 : Collecter
                // Si le filtre est passé, on garde la valeur originale du dictionnaire,
                // qui est en MAJUSCULES (ex: "JAVA").
                .collect(Collectors.toSet());
    }
}