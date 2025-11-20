package com.hamza.job_tracker.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. On définit la "Chaîne de sécurité"
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // A. ACTIVATION DE CORS : On dit à Spring d'utiliser notre bean défini plus bas
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // B. DÉSACTIVATION CSRF : Indispensable pour les API REST sans session (Stateless)
                // Sinon vous aurez des erreurs 403 sur les POST/PUT
                .csrf(csrf -> csrf.disable())

                // C. GESTION DES AUTORISATIONS
                .authorizeHttpRequests(auth -> auth
                        // Autoriser l'accès public à certaines routes (login, register, etc.)
                        .requestMatchers("/api/**", "/auth/**").permitAll()
                        // Toutes les autres requêtes nécessitent une authentification (à adapter selon votre besoin)
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    // 2. Votre configuration CORS (Corrigée et intégrée ici pour être sûr qu'elle est vue)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Le motif magique pour Minikube et Localhost
        configuration.setAllowedOriginPatterns(List.of("*")); // En DEV : On autorise TOUT.
        // En PROD, remplacez par : List.of("http://localhost:*", "http://127.0.0.1:*")

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}