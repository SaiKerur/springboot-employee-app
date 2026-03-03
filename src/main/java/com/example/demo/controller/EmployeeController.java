package com.example.demo.controller;

import com.example.demo.dto.EmployeeResponseDTO;
import com.example.demo.dto.*;
import com.example.demo.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService employeeService) {
        this.service = employeeService;
    }

    @PostMapping
    public EmployeeResponseDTO create(@Valid @RequestBody EmployeeRequestDTO dto){
        return service.save(dto);
    }

    @GetMapping
    public List<EmployeeResponseDTO> getAll(){
        return service.getAll();
    }
}
