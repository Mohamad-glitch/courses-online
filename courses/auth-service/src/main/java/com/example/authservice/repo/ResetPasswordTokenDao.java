package com.example.authservice.repo;

import com.example.authservice.model.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResetPasswordTokenDao extends JpaRepository<ResetPasswordToken, UUID> {

    ResetPasswordToken findResetPasswordTokenByToken(String token);
}
