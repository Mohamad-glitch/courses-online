package com.example.courseservice.exception;

public class TagExists extends RuntimeException {
    public TagExists(String message) {
        super(message);
    }
}
