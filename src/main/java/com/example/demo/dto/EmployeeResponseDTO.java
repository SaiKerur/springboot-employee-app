package com.example.demo.dto;

public record EmployeeResponseDTO(
        Long id,
        String name,
        String email,
        Double salary
) {
}
