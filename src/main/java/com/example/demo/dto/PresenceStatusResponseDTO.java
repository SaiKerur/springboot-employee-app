package com.example.demo.dto;

import com.example.demo.model.PresenceState;

import java.time.LocalDateTime;

public record PresenceStatusResponseDTO(
        int id,
        PresenceState state,
        LocalDateTime lastSeenAt,
        String source,
        Integer employeeId,
        String employeeName
) {
}
