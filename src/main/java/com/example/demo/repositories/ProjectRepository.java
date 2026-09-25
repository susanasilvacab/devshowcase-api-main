package com.example.demo.repositories;

import com.example.demo.entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findByTechnologies_NameIgnoreCase(
            String technology,
            Pageable pageable
    );
}
