package com.example.demo.service;

import com.example.demo.dto.ProjectRequestDTO;
import com.example.demo.dto.ProjectResponseDTO;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Project;
import com.example.demo.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

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

        Project updated = projectRepository.save(project);
        log.info("Project updated with id: {}", updated.getId());
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        projectRepository.delete(project);
        log.info("Project deleted with id: {}", id);
    }

    private ProjectResponseDTO toResponse(Project project) {
        return new ProjectResponseDTO(
                project.getId(),
                project.getTitle(),
                project.getSummary(),
                project.getBudget()
        );
    }
}
