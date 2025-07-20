package com.example.courseservice.dto;

import jakarta.validation.constraints.NotEmpty;

public record UpdateCourse(

        @NotEmpty(message = "Title is required")
        String title,

        @NotEmpty(message = "Description is required")
        String description,

        @NotEmpty(message = "Category is required")
        String category

) {
}
