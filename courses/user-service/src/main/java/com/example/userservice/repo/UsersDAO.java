package com.example.userservice.repo;

import com.example.userservice.model.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsersDAO extends JpaRepository<Users, UUID> {

    boolean existsUsersByEmail(@NotBlank @Email String email);

    Users findUsersByEmail(String email);
}
