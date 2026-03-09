package com.example.demo.service;

import com.example.demo.dto.EmployeeResponseDTO;
import com.example.demo.dto.EmployeeRequestDTO;
import com.example.demo.dto.EmployeeProfileSummaryDTO;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Department;
import com.example.demo.model.Employee;
import com.example.demo.model.Project;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;

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
        assignDepartment(employee, resolveDepartment(dto.departmentId()));
        syncProjects(employee, dto.projectIds());
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
    public EmployeeResponseDTO getById(int id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return toResponse(employee);
    }

    @Override
    @Transactional
    public EmployeeResponseDTO update(int id, EmployeeRequestDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        employeeRepository.findByEmailIgnoreCase(dto.email())
                .filter(existing -> existing.getId() != id)
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Employee already exists with email: " + dto.email());
                });

        employee.setName(dto.name());
        employee.setEmail(dto.email());
        employee.setSalary(dto.salary());
        assignDepartment(employee, resolveDepartment(dto.departmentId()));
        syncProjects(employee, dto.projectIds());

        Employee updatedEmployee = employeeRepository.save(employee);
        log.info("Employee updated with id: {}", updatedEmployee.getId());
        return toResponse(updatedEmployee);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        if (employee.getDepartment() != null) {
            employee.getDepartment().getEmployees().remove(employee);
        }
        for (Project project : new LinkedHashSet<>(employee.getProjects())) {
            employee.removeProject(project);
        }
        employeeRepository.delete(employee);
        log.info("Employee deleted with id: {}", id);
    }

    private EmployeeResponseDTO toResponse(Employee employee) {
        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getSalary(),
                employee.getDepartment() != null ? employee.getDepartment().getId() : null,
                employee.getDepartment() != null ? employee.getDepartment().getName() : null,
                employee.getProjects().stream().map(Project::getId).collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new)),
                employee.getProfile() == null
                        ? null
                        : new EmployeeProfileSummaryDTO(
                        employee.getProfile().getId(),
                        employee.getProfile().getPhoneNumber(),
                        employee.getProfile().getAddress(),
                        employee.getProfile().getEmergencyContact()
                )
        );
    }

    private Department resolveDepartment(Integer departmentId) {
        if (departmentId == null) {
            return null;
        }
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + departmentId));
    }

    private void assignDepartment(Employee employee, Department targetDepartment) {
        Department currentDepartment = employee.getDepartment();
        if (currentDepartment != null && currentDepartment != targetDepartment) {
            currentDepartment.getEmployees().remove(employee);
        }

        if (targetDepartment == null) {
            employee.setDepartment(null);
            return;
        }

        if (!targetDepartment.getEmployees().contains(employee)) {
            targetDepartment.addEmployee(employee);
        } else {
            employee.setDepartment(targetDepartment);
        }
    }

    private void syncProjects(Employee employee, Set<Integer> requestedProjectIds) {
        Set<Integer> targetIds = requestedProjectIds == null
                ? Set.of()
                : new LinkedHashSet<>(requestedProjectIds);

        for (Project project : new LinkedHashSet<>(employee.getProjects())) {
            if (!targetIds.contains(project.getId())) {
                employee.removeProject(project);
            }
        }

        for (Integer projectId : targetIds) {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
            employee.addProject(project);
        }
    }
}
