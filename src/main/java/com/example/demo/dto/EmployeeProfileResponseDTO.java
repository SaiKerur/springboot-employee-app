package com.example.demo.dto;

public record EmployeeProfileResponseDTO(
        int id,
        String phoneNumber,
        String address,
        String emergencyContact,
        int employeeId,
        String employeeName
) {
}
