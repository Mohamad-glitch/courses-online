package com.example.courseservice.dao;

import com.example.courseservice.model.Sections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SectionsDao extends JpaRepository<Sections, UUID> {
}
