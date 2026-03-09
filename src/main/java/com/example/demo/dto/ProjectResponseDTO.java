package com.example.demo.dto;

import java.util.Set;

public record ProjectResponseDTO(
        int id,
        String title,
        String summary,
        Double budget,
        Set<Integer> employeeIds
) {
}
