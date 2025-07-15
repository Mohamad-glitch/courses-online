package com.example.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewPasswordDto(
        @NotBlank(message = "password is required")
        @Size(min = 6, message = "Password should contain at least 6 characters")
        String password
) {
}
