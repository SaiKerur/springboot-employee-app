package com.example.demo.service;

import com.example.demo.dto.TaskRequestDTO;
import com.example.demo.dto.TaskResponseDTO;

import java.util.List;

public interface TaskItemService {

    TaskResponseDTO save(TaskRequestDTO task);

    List<TaskResponseDTO> getAll();

    TaskResponseDTO getById(int id);

    TaskResponseDTO update(int id, TaskRequestDTO task);

    void deleteById(int id);
}
