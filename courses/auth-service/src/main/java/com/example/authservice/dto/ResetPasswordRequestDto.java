package com.example.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequestDto(

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email")
        String email
) {
}
