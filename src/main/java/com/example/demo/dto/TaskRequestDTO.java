package com.example.demo.dto;

import com.example.demo.model.TaskPriority;
import com.example.demo.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskRequestDTO(
        @NotBlank(message = "Task title cannot be empty")
        String title,

        String description,

        @NotNull(message = "Task status cannot be null")
        TaskStatus status,

        @NotNull(message = "Task priority cannot be null")
        TaskPriority priority,

        LocalDate dueDate,

        @NotNull(message = "Assigned employee id cannot be null")
        Integer assignedEmployeeId,

        Integer projectId
) {
}
