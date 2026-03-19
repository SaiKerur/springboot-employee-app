package com.example.demo.service;

import com.example.demo.dto.WorkLogRequestDTO;
import com.example.demo.dto.WorkLogResponseDTO;

import java.util.List;

public interface WorkLogService {

    WorkLogResponseDTO save(WorkLogRequestDTO workLog);

    List<WorkLogResponseDTO> getAll();

    WorkLogResponseDTO getById(int id);

    WorkLogResponseDTO update(int id, WorkLogRequestDTO workLog);

    void deleteById(int id);
}
