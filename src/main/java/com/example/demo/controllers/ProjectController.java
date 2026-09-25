package com.example.demo.controllers;

import com.example.demo.dto.FeedbackRequest;
import com.example.demo.entities.Feedback;
import com.example.demo.entities.Project;
import com.example.demo.repositories.ProjectRepository;
import com.example.demo.services.ProjectService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final ProjectService projectService;

    public ProjectController(
            ProjectRepository projectRepository,
            ProjectService projectService
    ) {
        this.projectRepository = projectRepository;
        this.projectService = projectService;
    }

    /*
     * GET /api/projects
     *
     * Pode usar:
     * /api/projects?page=0&size=10
     *
     * ou:
     * /api/projects?technology=Java&page=0&size=10
     */
    @GetMapping
    public ResponseEntity<Page<Project>> getAllProjects(

            @RequestParam(required = false)
            String technology,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                projectService.findAll(
                        technology,
                        pageable
                )
        );
    }

    /*
     * PUT /api/projects/{id}/upvote
     */
    @PutMapping("/{id}/upvote")
    public ResponseEntity<Project> upvote(
            @PathVariable Long id
    ) {

        Project project =
                projectService.addUpvote(id);

        return ResponseEntity.ok(project);
    }

    /*
     * POST /api/projects/{id}/feedbacks
     */
    @PostMapping("/{id}/feedbacks")
    public ResponseEntity<Feedback> addFeedback(
            @PathVariable Long id,

            @Valid
            @RequestBody FeedbackRequest request
    ) {

        Feedback feedback =
                projectService.addFeedback(
                        id,
                        request.getRating(),
                        request.getComment()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(feedback);
    }

    /*
     * GET /api/projects/{id}
     *
     * Útil para testar 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                projectService.findById(id)
        );
    }

    /*
     * POST /api/projects
     *
     * Endpoint da primeira etapa.
     */
    @PostMapping
    public ResponseEntity<Project> createProject(
            @RequestBody Project project
    ) {

        if (project.getTitle() == null ||
                project.getTitle().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "O título do projeto é obrigatório."
            );
        }

        if (project.getUrl() == null ||
                !project.getUrl().startsWith("http")) {

            throw new IllegalArgumentException(
                    "A URL do projeto é inválida."
            );
        }

        Project saved =
                projectRepository.save(project);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }
}
