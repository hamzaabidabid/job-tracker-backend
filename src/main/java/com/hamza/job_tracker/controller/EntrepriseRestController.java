package com.hamza.job_tracker.controller;

import com.hamza.job_tracker.entity.Entreprise;
import com.hamza.job_tracker.service.EntrepriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/entreprises")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080"})
public class EntrepriseRestController {
@Autowired
    private  EntrepriseService entrepriseService;


    @GetMapping
    public List<Entreprise> getAllEntreprises() {
        return entrepriseService.getAllEntreprises();
    }
}