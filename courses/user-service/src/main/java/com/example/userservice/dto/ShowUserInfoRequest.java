package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ShowUserInfoRequest(

        @Email(message = "Invalid email")
        @NotNull(message = "Email is required")
        String email

) {
}
