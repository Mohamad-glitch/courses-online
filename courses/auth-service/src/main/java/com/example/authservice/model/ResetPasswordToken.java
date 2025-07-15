package com.example.authservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Table(name = "reset_tokens")
@Entity
@Data
public class ResetPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @Column(name = "token",  unique = true, nullable = false)
    private String token;

    @Column(name = "accessed")
    private boolean accessed;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "accessed_at")
    private Date accessedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "user_email", nullable = false)
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expires_at", nullable = false)
    private Date expiredAt;

}
