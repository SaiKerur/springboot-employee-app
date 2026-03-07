package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.DepartmentRequestDTO;
import com.example.demo.dto.DepartmentResponseDTO;
import com.example.demo.service.DepartmentService;
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
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService service;

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> create(@Valid @RequestBody DepartmentRequestDTO dto) {
        DepartmentResponseDTO department = service.save(dto);
        ApiResponse<DepartmentResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "Department created successfully",
                department
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponseDTO>>> getAll() {
        List<DepartmentResponseDTO> departments = service.getAll();
        ApiResponse<List<DepartmentResponseDTO>> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Departments fetched successfully",
                departments
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> getById(@PathVariable int id) {
        DepartmentResponseDTO department = service.getById(id);
        ApiResponse<DepartmentResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Department fetched successfully",
                department
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> update(
            @PathVariable int id,
            @Valid @RequestBody DepartmentRequestDTO dto
    ) {
        DepartmentResponseDTO updated = service.update(id, dto);
        ApiResponse<DepartmentResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Department updated successfully",
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
            throw new IllegalArgumentException("To delete department, set confirm=true");
        }
        service.deleteById(id);
        ApiResponse<String> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Department deleted successfully",
                "Deleted department with id: " + id
        );
        return ResponseEntity.ok(response);
    }
}
