package com.example.demo.service;

import com.example.demo.dto.DepartmentRequestDTO;
import com.example.demo.dto.DepartmentResponseDTO;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Department;
import com.example.demo.model.Employee;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public DepartmentResponseDTO save(DepartmentRequestDTO dto) {
        departmentRepository.findByNameIgnoreCase(dto.name()).ifPresent(existing -> {
            throw new DuplicateResourceException("Department already exists with name: " + dto.name());
        });

        Department department = new Department();
        department.setName(dto.name());
        department.setDescription(dto.description());
        syncEmployees(department, dto.employeeIds());

        Department saved = departmentRepository.save(department);
        log.info("Department created with id: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponseDTO> getAll() {
        return departmentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponseDTO getById(int id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return toResponse(department);
    }

    @Override
    @Transactional
    public DepartmentResponseDTO update(int id, DepartmentRequestDTO dto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        departmentRepository.findByNameIgnoreCase(dto.name())
                .filter(existing -> existing.getId() != id)
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Department already exists with name: " + dto.name());
                });

        department.setName(dto.name());
        department.setDescription(dto.description());
        syncEmployees(department, dto.employeeIds());

        Department updated = departmentRepository.save(department);
        log.info("Department updated with id: {}", updated.getId());
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        List<Employee> departmentEmployees = new ArrayList<>(department.getEmployees());
        for (Employee employee : departmentEmployees) {
            department.removeEmployee(employee);
        }
        departmentRepository.delete(department);
        log.info("Department deleted with id: {}", id);
    }

    private DepartmentResponseDTO toResponse(Department department) {
        return new DepartmentResponseDTO(
                department.getId(),
                department.getName(),
                department.getDescription(),
                department.getEmployees().stream().map(Employee::getId).toList()
        );
    }

    private void syncEmployees(Department department, Set<Integer> requestedEmployeeIds) {
        Set<Integer> targetIds = requestedEmployeeIds == null
                ? Set.of()
                : new LinkedHashSet<>(requestedEmployeeIds);

        List<Employee> currentEmployees = new ArrayList<>(department.getEmployees());
        for (Employee employee : currentEmployees) {
            if (!targetIds.contains(employee.getId())) {
                department.removeEmployee(employee);
            }
        }

        for (Integer employeeId : targetIds) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
            if (employee.getDepartment() != department) {
                if (employee.getDepartment() != null) {
                    employee.getDepartment().getEmployees().remove(employee);
                }
                if (!department.getEmployees().contains(employee)) {
                    department.addEmployee(employee);
                } else {
                    employee.setDepartment(department);
                }
            }
        }
    }
}
