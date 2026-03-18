package com.example.demo.dto;

public record EmployeePaymentResultDTO(
        Integer employeeId,
        String employeeName,
        String employeeEmail,
        Double amount,
        String status,
        String transactionId,
        String message
) {
}
