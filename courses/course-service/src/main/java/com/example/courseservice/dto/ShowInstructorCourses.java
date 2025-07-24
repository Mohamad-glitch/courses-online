package com.example.courseservice.dto;

public record ShowInstructorCourses(
        String id,
        String title,
        String description,
        String instructorEmail,
        String category,
        String rating,
        String price,
        String image_url
) {
}
