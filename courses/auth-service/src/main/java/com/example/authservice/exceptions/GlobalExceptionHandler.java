package com.example.authservice.exceptions;

import io.grpc.StatusRuntimeException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Map<String, String>> handleJwtException(JwtException ex){
        log.warn(ex.getMessage());
        Map<String,String> error = new HashMap<>();
        error.put("error",ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>  handleMethodArgumentNotValid(MethodArgumentNotValidException ex){

        log.warn(ex.getMessage());
        Map<String,String> map = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error -> map.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExists(UserAlreadyExists ex){
        log.warn(ex.getMessage());
        Map<String,String> map = new HashMap<>();
        map.put("error",ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(map);
    }

    @ExceptionHandler(StatusRuntimeException.class)
    public ResponseEntity<Map<String, String>> handleStatusRuntimeException(StatusRuntimeException ex){
        log.error("Failed to connect to User service: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "User service is unavailable, please try again later"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex){
        log.warn(ex.getMessage());
        Map<String,String> map = new HashMap<>();
        map.put("error",ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(map);
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFound ex){
        log.warn(ex.getMessage());
        Map<String,String> map = new HashMap<>();
        map.put("error",ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(map);
    }

    @ExceptionHandler(InvalidPasswordOrEmail.class)
    public ResponseEntity<Map<String, String>> handleInvalidPasswordOrEmail(InvalidPasswordOrEmail ex){
        log.warn(ex.getMessage());
        Map<String,String> map = new HashMap<>();
        map.put("error",ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(map);
    }

}
