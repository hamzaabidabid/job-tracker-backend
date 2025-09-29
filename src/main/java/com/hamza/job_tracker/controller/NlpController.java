package com.hamza.job_tracker.controller;

import com.hamza.job_tracker.service.NlpService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/nlp")
@CrossOrigin(origins = "http://localhost:4200")
public class NlpController {

    private final NlpService nlpService;

    public NlpController(NlpService nlpService) {
        this.nlpService = nlpService;
    }

    @PostMapping("/extract-skills")
    public Set<String> extractSkillsFromText(@RequestBody String description) {
        return nlpService.extractSkills(description);
    }
}