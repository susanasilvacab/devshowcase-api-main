package com.example.demo.repositories;

import com.example.demo.entities.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnologyRepository
extends JpaRepository<Technology,Long>{
}