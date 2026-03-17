package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DepartmentSalaryPaymentRequestDTO(
        @NotNull(message = "Department id is required")
        Integer departmentId,

        @NotBlank(message = "Payment provider is required")
        String paymentProvider,

        @NotBlank(message = "Salary month is required")
        @Pattern(
                regexp = "^\\d{4}-(0[1-9]|1[0-2])$",
                message = "Salary month must be in yyyy-MM format"
        )
        String salaryMonth
) {
}
