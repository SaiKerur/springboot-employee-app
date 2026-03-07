package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record DepartmentRequestDTO(
        @NotBlank(message = "Department name cannot be empty")
        String name,

        String description
) {
}
