package com.example.demo.controllers;

import com.example.demo.entities.Profile;
import com.example.demo.repositories.ProfileRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileRepository profileRepository;

    public ProfileController(
            ProfileRepository profileRepository
    ) {
        this.profileRepository = profileRepository;
    }

    @PostMapping
    public ResponseEntity<Profile> createProfile(
            @RequestBody Profile profile
    ) {

        if (profile.getName() == null ||
                profile.getName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "O nome é obrigatório."
            );
        }

        if (profile.getEmail() == null ||
                !profile.getEmail().contains("@")) {

            throw new IllegalArgumentException(
                    "O e-mail é inválido."
            );
        }

        Profile saved =
                profileRepository.save(profile);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profile> getProfileById(
            @PathVariable Long id
    ) {

        Profile profile =
                profileRepository.findById(id)
                        .orElseThrow(() ->
                                new com.example.demo.exceptions
                                        .ResourceNotFoundException(
                                        "Perfil não encontrado: " + id
                                )
                        );

        return ResponseEntity.ok(profile);
    }
}
