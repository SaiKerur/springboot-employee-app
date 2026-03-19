package com.example.demo.dto;

import com.example.demo.model.NotificationType;

import java.time.LocalDateTime;

public record LiveNotificationResponseDTO(
        int id,
        String message,
        NotificationType type,
        Boolean readFlag,
        LocalDateTime deliveredAt,
        Integer employeeId,
        String employeeName
) {
}
