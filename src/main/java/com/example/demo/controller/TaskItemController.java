package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.TaskRequestDTO;
import com.example.demo.dto.TaskResponseDTO;
import com.example.demo.service.TaskItemService;
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
@RequestMapping("/task")
public class TaskItemController {

    private final TaskItemService service;

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponseDTO>> create(@Valid @RequestBody TaskRequestDTO dto) {
        TaskResponseDTO task = service.save(dto);
        ApiResponse<TaskResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "Task created successfully",
                task
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponseDTO>>> getAll() {
        List<TaskResponseDTO> tasks = service.getAll();
        ApiResponse<List<TaskResponseDTO>> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Tasks fetched successfully",
                tasks
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> getById(@PathVariable int id) {
        TaskResponseDTO task = service.getById(id);
        ApiResponse<TaskResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Task fetched successfully",
                task
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> update(
            @PathVariable int id,
            @Valid @RequestBody TaskRequestDTO dto
    ) {
        TaskResponseDTO updated = service.update(id, dto);
        ApiResponse<TaskResponseDTO> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Task updated successfully",
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
            throw new IllegalArgumentException("To delete task, set confirm=true");
        }
        service.deleteById(id);
        ApiResponse<String> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Task deleted successfully",
                "Deleted task with id: " + id
        );
        return ResponseEntity.ok(response);
    }
}
