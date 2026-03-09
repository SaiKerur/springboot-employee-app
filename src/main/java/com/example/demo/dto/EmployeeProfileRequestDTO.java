package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmployeeProfileRequestDTO(
        @NotBlank(message = "Phone number cannot be empty")
        String phoneNumber,

        @NotBlank(message = "Address cannot be empty")
        String address,

        String emergencyContact,

        @NotNull(message = "Employee id is required")
        Integer employeeId
) {
}
