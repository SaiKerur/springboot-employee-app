package com.example.demo.dto;

import com.example.demo.model.AttendanceStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AttendanceResponseDTO(
        int id,
        Integer employeeId,
        String employeeName,
        LocalDate workDate,
        LocalDateTime checkInAt,
        LocalDateTime checkOutAt,
        AttendanceStatus status
) {
}
