package com.example.demo.service;

import com.example.demo.dto.DepartmentRequestDTO;
import com.example.demo.dto.DepartmentResponseDTO;

import java.util.List;

public interface DepartmentService {

    DepartmentResponseDTO save(DepartmentRequestDTO department);

    List<DepartmentResponseDTO> getAll();

    DepartmentResponseDTO getById(int id);

    DepartmentResponseDTO update(int id, DepartmentRequestDTO department);

    void deleteById(int id);
}
