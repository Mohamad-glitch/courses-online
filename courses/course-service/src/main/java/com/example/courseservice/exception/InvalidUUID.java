package com.example.courseservice.exception;

public class InvalidUUID extends RuntimeException {
    public InvalidUUID(String message) {
        super(message);
    }
}
