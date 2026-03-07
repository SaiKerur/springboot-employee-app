package com.example.demo.dto;

public record ProjectResponseDTO(
        int id,
        String title,
        String summary,
        Double budget
) {
}
