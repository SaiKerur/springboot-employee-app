package com.example.demo.exception;

import com.example.demo.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleBadRequest(IllegalArgumentException exception) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", exception.getMessage());

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad request",
                error
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleConflict(DuplicateResourceException exception) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", exception.getMessage());

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Resource already exists",
                error
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleNotFound(ResourceNotFoundException exception){
        Map<String, Object> error = new HashMap<>();
        error.put("error", exception.getMessage());

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Resource not found",
                error
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException exception){

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError ->
                errors.put(fieldError.getField(),
                        fieldError.getDefaultMessage()));

        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors
        );
        return ResponseEntity.badRequest().body(response);
    }


}
