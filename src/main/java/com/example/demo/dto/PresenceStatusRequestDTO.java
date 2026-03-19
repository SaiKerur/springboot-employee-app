package com.example.demo.dto;

import com.example.demo.model.PresenceState;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PresenceStatusRequestDTO(
        @NotNull(message = "Presence state cannot be null")
        PresenceState state,

        LocalDateTime lastSeenAt,

        String source,

        @NotNull(message = "Employee id cannot be null")
        Integer employeeId
) {
}
