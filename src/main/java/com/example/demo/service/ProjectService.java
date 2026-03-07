package com.example.demo.service;

import com.example.demo.dto.ProjectRequestDTO;
import com.example.demo.dto.ProjectResponseDTO;

import java.util.List;

public interface ProjectService {

    ProjectResponseDTO save(ProjectRequestDTO project);

    List<ProjectResponseDTO> getAll();

    ProjectResponseDTO getById(int id);

    ProjectResponseDTO update(int id, ProjectRequestDTO project);

    void deleteById(int id);
}
