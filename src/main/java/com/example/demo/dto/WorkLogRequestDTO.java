package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record WorkLogRequestDTO(
        @NotBlank(message = "Activity cannot be empty")
        String activity,

        @NotNull(message = "Duration cannot be null")
        @Min(value = 1, message = "Duration must be at least 1 minute")
        Integer durationMinutes,

        @NotNull(message = "Work date cannot be null")
        LocalDate workDate,

        @NotNull(message = "Employee id cannot be null")
        Integer employeeId
) {
}
