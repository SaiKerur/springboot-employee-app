package com.example.demo.dto;

import java.time.LocalDate;

public record WorkLogResponseDTO(
        int id,
        String activity,
        Integer durationMinutes,
        LocalDate workDate,
        Integer employeeId,
        String employeeName
) {
}
