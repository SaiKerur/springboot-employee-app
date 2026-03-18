package com.example.demo.dto;

import com.example.demo.model.TaskPriority;
import com.example.demo.model.TaskStatus;

import java.time.LocalDate;

public record TaskResponseDTO(
        int id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDate dueDate,
        Integer assignedEmployeeId,
        String assignedEmployeeName,
        Integer projectId,
        String projectTitle
) {
}
