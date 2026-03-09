package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.EmployeeProfileRequestDTO;
import com.example.demo.dto.EmployeeProfileResponseDTO;
import com.example.demo.service.EmployeeProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/employee-profile")
public class EmployeeProfileController {

    private final EmployeeProfileService service;

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeProfileResponseDTO>> create(@Valid @RequestBody EmployeeProfileRequestDTO dto) {
        EmployeeProfileResponseDTO profile = service.save(dto);
        ApiResponse<EmployeeProfileResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "Employee profile created successfully",
                profile
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeProfileResponseDTO>>> getAll() {
        List<EmployeeProfileResponseDTO> profiles = service.getAll();
        ApiResponse<List<EmployeeProfileResponseDTO>> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Employee profiles fetched successfully",
                profiles
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeProfileResponseDTO>> getById(@PathVariable int id) {
        EmployeeProfileResponseDTO profile = service.getById(id);
        ApiResponse<EmployeeProfileResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Employee profile fetched successfully",
                profile
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeProfileResponseDTO>> update(
            @PathVariable int id,
            @Valid @RequestBody EmployeeProfileRequestDTO dto
    ) {
        EmployeeProfileResponseDTO updated = service.update(id, dto);
        ApiResponse<EmployeeProfileResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Employee profile updated successfully",
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
            throw new IllegalArgumentException("To delete employee profile, set confirm=true");
        }
        service.deleteById(id);
        ApiResponse<String> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Employee profile deleted successfully",
                "Deleted employee profile with id: " + id
        );
        return ResponseEntity.ok(response);
    }
}
