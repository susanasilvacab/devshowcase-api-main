package com.example.demo.controllers;

import com.example.demo.entities.Technology;
import com.example.demo.repositories.TechnologyRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technologies")
public class TechnologyController {

    private final TechnologyRepository technologyRepository;

    public TechnologyController(
            TechnologyRepository technologyRepository
    ) {
        this.technologyRepository = technologyRepository;
    }

    @PostMapping
    public ResponseEntity<Technology> createTechnology(
            @RequestBody Technology technology
    ) {

        if (technology.getName() == null ||
                technology.getName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "O nome da tecnologia é obrigatório."
            );
        }

        Technology saved =
                technologyRepository.save(technology);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Technology>> getAllTechnologies() {

        return ResponseEntity.ok(
                technologyRepository.findAll()
        );
    }
}
