package com.example.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email")
        String email
        ,
        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password should be at lest  6 characters")
        String password
) {
}
