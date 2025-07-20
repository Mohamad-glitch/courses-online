package com.example.courseservice.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record GetAllCoursesWithInstructorId(
        @NotNull(message = "Email is required")
        @Email(message = "Invalid email")
        String email
) {
}
