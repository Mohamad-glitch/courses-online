package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UpdateUserInfoRequest(
        @Email
        @NotEmpty
        String email,
        @NotEmpty
        @Size(min = 6)
        String fullName,
        String bio
) {
}
