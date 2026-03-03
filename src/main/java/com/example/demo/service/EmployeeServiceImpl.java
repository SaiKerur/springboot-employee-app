package com.example.demo.service;

import com.example.demo.dto.EmployeeResponseDTO;
import com.example.demo.dto.*;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeResponseDTO save(EmployeeRequestDTO dto) {

        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setSalary(dto.getSalary());

        Employee savedEmployee = employeeRepository.save(employee);

        return new EmployeeResponseDTO(
                savedEmployee.getId(),
                savedEmployee.getName(),
                savedEmployee.getEmail(),
                savedEmployee.getSalary()
        );

    }

    @Override
    public List<EmployeeResponseDTO> getAll() {
        return employeeRepository.findAll().stream().map(emp -> new EmployeeResponseDTO(
                emp.getId(),
                emp.getName(),
                emp.getEmail(),
                emp.getSalary()
        )).collect(Collectors.toList());
    }
}
