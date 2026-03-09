package com.example.demo.dto;

import java.util.Set;

public record EmployeeResponseDTO(
        int id,
        String name,
        String email,
        Double salary,
        Integer departmentId,
        String departmentName,
        Set<Integer> projectIds,
        EmployeeProfileSummaryDTO profile
) {
}
