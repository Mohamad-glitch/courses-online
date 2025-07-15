package com.example.authservice.exceptions;

public class UrlHasBeenUsed extends RuntimeException {
    public UrlHasBeenUsed(String message) {
        super(message);
    }
}
