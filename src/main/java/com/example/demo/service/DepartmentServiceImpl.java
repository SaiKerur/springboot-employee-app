package com.example.demo.service;

import com.example.demo.dto.DepartmentRequestDTO;
import com.example.demo.dto.DepartmentResponseDTO;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Department;
import com.example.demo.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public DepartmentResponseDTO save(DepartmentRequestDTO dto) {
        departmentRepository.findByNameIgnoreCase(dto.name()).ifPresent(existing -> {
            throw new DuplicateResourceException("Department already exists with name: " + dto.name());
        });

        Department department = new Department();
        department.setName(dto.name());
        department.setDescription(dto.description());

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

        Department updated = departmentRepository.save(department);
        log.info("Department updated with id: {}", updated.getId());
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        departmentRepository.delete(department);
        log.info("Department deleted with id: {}", id);
    }

    private DepartmentResponseDTO toResponse(Department department) {
        return new DepartmentResponseDTO(
                department.getId(),
                department.getName(),
                department.getDescription()
        );
    }
}
