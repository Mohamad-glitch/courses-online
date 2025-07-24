package com.example.courseservice.dao;

import com.example.courseservice.dto.ShowInstructorCourses;
import com.example.courseservice.model.Courses;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CoursesDao extends JpaRepository<Courses, UUID> {

    List<Courses> findCoursesByInstructorEmail(@NotNull(message = "Email is required") @Email String email);

    Courses findCoursesById(UUID id);

    @Query("select DISTINCT  category from Courses  ")
    List<String> getCoursesByRandomCategorise();

    @Query(value = "SELECT * FROM courses WHERE category = :category LIMIT 5", nativeQuery = true)
    List<Courses> findCoursesByCategory(@Param("category") String category);
}
