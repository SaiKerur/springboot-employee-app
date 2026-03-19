package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.PresenceStatusRequestDTO;
import com.example.demo.dto.PresenceStatusResponseDTO;
import com.example.demo.service.PresenceStatusService;
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
@RequestMapping("/presence-status")
public class PresenceStatusController {

    private final PresenceStatusService service;

    @PostMapping
    public ResponseEntity<ApiResponse<PresenceStatusResponseDTO>> create(@Valid @RequestBody PresenceStatusRequestDTO dto) {
        PresenceStatusResponseDTO presence = service.save(dto);
        ApiResponse<PresenceStatusResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "Presence status created successfully",
                presence
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PresenceStatusResponseDTO>>> getAll() {
        List<PresenceStatusResponseDTO> presenceStatuses = service.getAll();
        ApiResponse<List<PresenceStatusResponseDTO>> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Presence statuses fetched successfully",
                presenceStatuses
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PresenceStatusResponseDTO>> getById(@PathVariable int id) {
        PresenceStatusResponseDTO presence = service.getById(id);
        ApiResponse<PresenceStatusResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Presence status fetched successfully",
                presence
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PresenceStatusResponseDTO>> update(
            @PathVariable int id,
            @Valid @RequestBody PresenceStatusRequestDTO dto
    ) {
        PresenceStatusResponseDTO updated = service.update(id, dto);
        ApiResponse<PresenceStatusResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Presence status updated successfully",
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
            throw new IllegalArgumentException("To delete presence status, set confirm=true");
        }
        service.deleteById(id);
        ApiResponse<String> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Presence status deleted successfully",
                "Deleted presence status with id: " + id
        );
        return ResponseEntity.ok(response);
    }
}
