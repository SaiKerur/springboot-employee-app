package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.AttendanceRequestDTO;
import com.example.demo.dto.AttendanceResponseDTO;
import com.example.demo.service.AttendanceRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/attendance")
public class AttendanceRecordController {

    private final AttendanceRecordService service;

    @PostMapping
    public ResponseEntity<ApiResponse<AttendanceResponseDTO>> create(@Valid @RequestBody AttendanceRequestDTO dto) {
        AttendanceResponseDTO attendance = service.save(dto);
        ApiResponse<AttendanceResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "Attendance record created successfully",
                attendance
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AttendanceResponseDTO>>> getAll() {
        List<AttendanceResponseDTO> records = service.getAll();
        ApiResponse<List<AttendanceResponseDTO>> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Attendance records fetched successfully",
                records
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AttendanceResponseDTO>> getById(@PathVariable int id) {
        AttendanceResponseDTO attendance = service.getById(id);
        ApiResponse<AttendanceResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Attendance record fetched successfully",
                attendance
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AttendanceResponseDTO>> update(
            @PathVariable int id,
            @Valid @RequestBody AttendanceRequestDTO dto
    ) {
        AttendanceResponseDTO updated = service.update(id, dto);
        ApiResponse<AttendanceResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Attendance record updated successfully",
                updated
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(
            @PathVariable int id,
            @RequestParam(defaultValue = "false") boolean confirm
    ) {
        if (!confirm) {
            throw new IllegalArgumentException("To delete attendance record, set confirm=true");
        }
        service.deleteById(id);
        ApiResponse<String> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Attendance record deleted successfully",
                "Deleted attendance record with id: " + id
        );
        return ResponseEntity.ok(response);
    }
}
