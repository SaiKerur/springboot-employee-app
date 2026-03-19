package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.WorkLogRequestDTO;
import com.example.demo.dto.WorkLogResponseDTO;
import com.example.demo.service.WorkLogService;
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
@RequestMapping("/work-log")
public class WorkLogController {

    private final WorkLogService service;

    @PostMapping
    public ResponseEntity<ApiResponse<WorkLogResponseDTO>> create(@Valid @RequestBody WorkLogRequestDTO dto) {
        WorkLogResponseDTO workLog = service.save(dto);
        ApiResponse<WorkLogResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "Work log created successfully",
                workLog
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WorkLogResponseDTO>>> getAll() {
        List<WorkLogResponseDTO> workLogs = service.getAll();
        ApiResponse<List<WorkLogResponseDTO>> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Work logs fetched successfully",
                workLogs
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkLogResponseDTO>> getById(@PathVariable int id) {
        WorkLogResponseDTO workLog = service.getById(id);
        ApiResponse<WorkLogResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Work log fetched successfully",
                workLog
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkLogResponseDTO>> update(
            @PathVariable int id,
            @Valid @RequestBody WorkLogRequestDTO dto
    ) {
        WorkLogResponseDTO updated = service.update(id, dto);
        ApiResponse<WorkLogResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Work log updated successfully",
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
            throw new IllegalArgumentException("To delete work log, set confirm=true");
        }
        service.deleteById(id);
        ApiResponse<String> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Work log deleted successfully",
                "Deleted work log with id: " + id
        );
        return ResponseEntity.ok(response);
    }
}
