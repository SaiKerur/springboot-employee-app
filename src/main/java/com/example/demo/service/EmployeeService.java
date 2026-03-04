package com.example.demo.service;

import com.example.demo.dto.EmployeeRequestDTO;
import com.example.demo.dto.EmployeeResponseDTO;

import java.util.List;

public interface EmployeeService {

    EmployeeResponseDTO save(EmployeeRequestDTO employee);

    List<EmployeeResponseDTO> getAll();
}
