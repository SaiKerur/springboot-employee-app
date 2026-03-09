package com.example.demo.dto;

public record EmployeeProfileSummaryDTO(
        int id,
        String phoneNumber,
        String address,
        String emergencyContact
) {
}
