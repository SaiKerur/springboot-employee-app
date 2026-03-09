package com.example.demo.service;

import com.example.demo.dto.EmployeeProfileRequestDTO;
import com.example.demo.dto.EmployeeProfileResponseDTO;

import java.util.List;

public interface EmployeeProfileService {

    EmployeeProfileResponseDTO save(EmployeeProfileRequestDTO employeeProfile);

    List<EmployeeProfileResponseDTO> getAll();

    EmployeeProfileResponseDTO getById(int id);

    EmployeeProfileResponseDTO update(int id, EmployeeProfileRequestDTO employeeProfile);

    void deleteById(int id);
}
