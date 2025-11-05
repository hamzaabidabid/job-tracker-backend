package com.hamza.job_tracker.controller;

import com.hamza.job_tracker.service.NlpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/nlp")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080","http://127.0.0.1"})

public class NlpController {
@Autowired
    private  NlpService nlpService;



    @PostMapping("/extract-skills")
    public Set<String> extractSkillsFromText(@RequestBody String description) {
        return nlpService.extractSkills(description);
    }
}