package com.example.courseservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreateCourse(

        @NotEmpty(message = "Title is required")
        String title,

        @NotEmpty(message = "Description is required")
        String description,

        @NotEmpty(message = "Email is required")
        @Email(message = "Invalid Email")
        String instructorEmail,

        @NotEmpty(message = "Category is required")
        String category,

        @NotEmpty(message = "Tag/s is/are required")
        List<String> tags,


        Double price,

        MultipartFile image

        ) {
}
