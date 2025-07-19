package com.example.userservice.dto;

public record ShowUserInfoDto(
        String email,
        String fullName,
        String bio
) {
}
