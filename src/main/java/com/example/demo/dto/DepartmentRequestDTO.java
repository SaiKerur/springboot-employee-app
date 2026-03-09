package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record DepartmentRequestDTO(
        @NotBlank(message = "Department name cannot be empty")
        String name,

        String description,

        Set<Integer> employeeIds
) {
}
