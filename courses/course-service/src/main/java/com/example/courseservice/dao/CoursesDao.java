package com.example.courseservice.dao;

import com.example.courseservice.model.Courses;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CoursesDao extends JpaRepository<Courses, UUID> {

    List<Courses> findCoursesByInstructorEmail(@NotNull(message = "Email is required") @Email String email);

    Courses findCoursesById(UUID id);

}
