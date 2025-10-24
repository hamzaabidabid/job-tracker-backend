package com.hamza.job_tracker.service;

import com.hamza.job_tracker.entity.Entreprise;
import com.hamza.job_tracker.repository.EntrepriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EntrepriseService {
    @Autowired
    private  EntrepriseRepository entrepriseRepository;

    public EntrepriseService(EntrepriseRepository entrepriseRepository) {
        this.entrepriseRepository = entrepriseRepository;
    }

    public List<Entreprise> getAllEntreprises() {
        return entrepriseRepository.findAll();
    }
}