package com.example.demo.service;

import com.example.demo.dto.EmployeeRequestDTO;
import com.example.demo.dto.EmployeeResponseDTO;

import java.util.List;

public interface EmployeeService {

    EmployeeResponseDTO save(EmployeeRequestDTO employee);

    List<EmployeeResponseDTO> getAll();

    EmployeeResponseDTO getById(Long id);

    EmployeeResponseDTO update(Long id, EmployeeRequestDTO employee);

    void deleteById(Long id);
}
