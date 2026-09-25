package com.example.demo.services;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.entities.Feedback;
import com.example.demo.entities.Project;
import com.example.demo.repositories.FeedbackRepository;
import com.example.demo.repositories.ProjectRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final FeedbackRepository feedbackRepository;

    public ProjectService(
            ProjectRepository projectRepository,
            FeedbackRepository feedbackRepository
    ) {
        this.projectRepository = projectRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public Page<Project> findAll(String technology, Pageable pageable) {

        if (technology != null && !technology.isBlank()) {
            return projectRepository
                    .findByTechnologies_NameIgnoreCase(technology, pageable);
        }

        return projectRepository.findAll(pageable);
    }

    public Project findById(Long id) {

       throw new ResourceNotFoundException(
        "Projeto não encontrado: " + id
);
    }

    @Transactional
    public Project addUpvote(Long id) {

        Project project = findById(id);

        if (project.getUpvotes() == null) {
            project.setUpvotes(0);
        }

        project.setUpvotes(project.getUpvotes() + 1);

        return projectRepository.save(project);
    }

    @Transactional
    public Feedback addFeedback(
            Long projectId,
            Integer rating,
            String comment
    ) {

        Project project = findById(projectId);

        Feedback feedback = new Feedback();

        feedback.setRating(rating);
        feedback.setComment(comment);
        feedback.setProject(project);

        Feedback savedFeedback =
                feedbackRepository.save(feedback);

        project.getFeedbacks().add(savedFeedback);

        double average = project.getFeedbacks()
                .stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);

        project.setAverageRating(
                Math.round(average * 100.0) / 100.0
        );

        projectRepository.save(project);

        return savedFeedback;
    }
}
