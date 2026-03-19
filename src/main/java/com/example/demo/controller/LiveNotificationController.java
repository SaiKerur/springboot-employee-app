package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.LiveNotificationRequestDTO;
import com.example.demo.dto.LiveNotificationResponseDTO;
import com.example.demo.service.LiveNotificationService;
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
@RequestMapping("/live-notification")
public class LiveNotificationController {

    private final LiveNotificationService service;

    @PostMapping
    public ResponseEntity<ApiResponse<LiveNotificationResponseDTO>> create(@Valid @RequestBody LiveNotificationRequestDTO dto) {
        LiveNotificationResponseDTO notification = service.save(dto);
        ApiResponse<LiveNotificationResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "Live notification created successfully",
                notification
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LiveNotificationResponseDTO>>> getAll() {
        List<LiveNotificationResponseDTO> notifications = service.getAll();
        ApiResponse<List<LiveNotificationResponseDTO>> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Live notifications fetched successfully",
                notifications
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LiveNotificationResponseDTO>> getById(@PathVariable int id) {
        LiveNotificationResponseDTO notification = service.getById(id);
        ApiResponse<LiveNotificationResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Live notification fetched successfully",
                notification
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LiveNotificationResponseDTO>> update(
            @PathVariable int id,
            @Valid @RequestBody LiveNotificationRequestDTO dto
    ) {
        LiveNotificationResponseDTO updated = service.update(id, dto);
        ApiResponse<LiveNotificationResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Live notification updated successfully",
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
            throw new IllegalArgumentException("To delete live notification, set confirm=true");
        }
        service.deleteById(id);
        ApiResponse<String> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Live notification deleted successfully",
                "Deleted live notification with id: " + id
        );
        return ResponseEntity.ok(response);
    }
}
