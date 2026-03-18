package com.example.demo.dto;

import com.example.demo.model.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AttendanceRequestDTO(
        @NotNull(message = "Employee id cannot be null")
        Integer employeeId,

        @NotNull(message = "Work date cannot be null")
        LocalDate workDate,

        LocalDateTime checkInAt,

        LocalDateTime checkOutAt,

        @NotNull(message = "Attendance status cannot be null")
        AttendanceStatus status
) {
}
