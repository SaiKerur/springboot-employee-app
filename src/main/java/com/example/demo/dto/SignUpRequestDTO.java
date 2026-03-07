package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequestDTO(
        @NotBlank(message = "Name cannot be empty")
        String name,

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email cannot be empty")
        String email,

        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, message = "Password must contain at least 8 characters")
        String password
) {
}
