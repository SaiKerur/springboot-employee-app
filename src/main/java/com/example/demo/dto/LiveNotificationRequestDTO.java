package com.example.demo.dto;

import com.example.demo.model.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record LiveNotificationRequestDTO(
        @NotBlank(message = "Message cannot be empty")
        String message,

        @NotNull(message = "Notification type cannot be null")
        NotificationType type,

        @NotNull(message = "Read flag cannot be null")
        Boolean readFlag,

        LocalDateTime deliveredAt,

        @NotNull(message = "Employee id cannot be null")
        Integer employeeId
) {
}
