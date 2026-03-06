package com.example.demo.service;

import com.example.demo.dto.EmployeeResponseDTO;
import com.example.demo.dto.EmployeeRequestDTO;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public EmployeeResponseDTO save(EmployeeRequestDTO dto) {
        employeeRepository.findByEmailIgnoreCase(dto.email()).ifPresent(employee -> {
            throw new DuplicateResourceException("Employee already exists with email: " + dto.email());
        });

        Employee employee = new Employee();
        employee.setName(dto.name());
        employee.setEmail(dto.email());
        employee.setSalary(dto.salary());
        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created with id: {}", savedEmployee.getId());
        return toResponse(savedEmployee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDTO> getAll() {
        return employeeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDTO getById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return toResponse(employee);
    }

    @Override
    @Transactional
    public EmployeeResponseDTO update(Long id, EmployeeRequestDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        employeeRepository.findByEmailIgnoreCase(dto.email())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Employee already exists with email: " + dto.email());
                });

        employee.setName(dto.name());
        employee.setEmail(dto.email());
        employee.setSalary(dto.salary());

        Employee updatedEmployee = employeeRepository.save(employee);
        log.info("Employee updated with id: {}", updatedEmployee.getId());
        return toResponse(updatedEmployee);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employeeRepository.delete(employee);
        log.info("Employee deleted with id: {}", id);
    }

    private EmployeeResponseDTO toResponse(Employee employee) {
        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getSalary()
        );
    }
}
