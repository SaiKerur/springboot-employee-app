package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Set;

public record ProjectRequestDTO(
        @NotBlank(message = "Project title cannot be empty")
        String title,

        String summary,

        @NotNull(message = "Budget cannot be null")
        @PositiveOrZero(message = "Budget must be positive or zero")
        Double budget,

        Set<Integer> employeeIds
) {
}
