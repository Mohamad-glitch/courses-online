package com.example.authservice.exceptions;

public class InvalidPasswordOrEmail extends RuntimeException {
    public InvalidPasswordOrEmail(String message) {
        super(message);
    }
}
