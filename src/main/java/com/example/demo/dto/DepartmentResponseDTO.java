package com.example.demo.dto;

import java.util.List;

public record DepartmentResponseDTO(
        int id,
        String name,
        String description,
        List<Integer> employeeIds
) {
}
