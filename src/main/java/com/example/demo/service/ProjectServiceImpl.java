package com.example.demo.service;

import com.example.demo.dto.ProjectRequestDTO;
import com.example.demo.dto.ProjectResponseDTO;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.model.Project;
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
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public ProjectResponseDTO save(ProjectRequestDTO dto) {
        projectRepository.findByTitleIgnoreCase(dto.title()).ifPresent(existing -> {
            throw new DuplicateResourceException("Project already exists with title: " + dto.title());
        });

        Project project = new Project();
        project.setTitle(dto.title());
        project.setSummary(dto.summary());
        project.setBudget(dto.budget());
        syncEmployees(project, dto.employeeIds());

        Project saved = projectRepository.save(project);
        log.info("Project created with id: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDTO> getAll() {
        return projectRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponseDTO getById(int id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        return toResponse(project);
    }

    @Override
    @Transactional
    public ProjectResponseDTO update(int id, ProjectRequestDTO dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        projectRepository.findByTitleIgnoreCase(dto.title())
                .filter(existing -> existing.getId() != id)
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Project already exists with title: " + dto.title());
                });

        project.setTitle(dto.title());
        project.setSummary(dto.summary());
        project.setBudget(dto.budget());
        syncEmployees(project, dto.employeeIds());

        Project updated = projectRepository.save(project);
        log.info("Project updated with id: {}", updated.getId());
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        for (Employee employee : new LinkedHashSet<>(project.getEmployees())) {
            project.removeEmployee(employee);
        }
        projectRepository.delete(project);
        log.info("Project deleted with id: {}", id);
    }

    private ProjectResponseDTO toResponse(Project project) {
        return new ProjectResponseDTO(
                project.getId(),
                project.getTitle(),
                project.getSummary(),
                project.getBudget(),
                project.getEmployees().stream()
                        .map(Employee::getId)
                        .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new))
        );
    }

    private void syncEmployees(Project project, Set<Integer> requestedEmployeeIds) {
        Set<Integer> targetIds = requestedEmployeeIds == null
                ? Set.of()
                : new LinkedHashSet<>(requestedEmployeeIds);

        for (Employee employee : new LinkedHashSet<>(project.getEmployees())) {
            if (!targetIds.contains(employee.getId())) {
                project.removeEmployee(employee);
            }
        }

        for (Integer employeeId : targetIds) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
            project.addEmployee(employee);
        }
    }
}
