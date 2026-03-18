package com.example.demo.service;

import com.example.demo.dto.TaskRequestDTO;
import com.example.demo.dto.TaskResponseDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.model.Project;
import com.example.demo.model.TaskItem;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.TaskItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskItemServiceImpl implements TaskItemService {

    private final TaskItemRepository taskItemRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public TaskResponseDTO save(TaskRequestDTO dto) {
        TaskItem taskItem = new TaskItem();
        mapRequestToEntity(taskItem, dto);
        TaskItem saved = taskItemRepository.save(taskItem);
        log.info("Task created with id: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getAll() {
        return taskItemRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponseDTO getById(int id) {
        TaskItem taskItem = taskItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return toResponse(taskItem);
    }

    @Override
    @Transactional
    public TaskResponseDTO update(int id, TaskRequestDTO dto) {
        TaskItem taskItem = taskItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        mapRequestToEntity(taskItem, dto);
        TaskItem updated = taskItemRepository.save(taskItem);
        log.info("Task updated with id: {}", updated.getId());
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        TaskItem taskItem = taskItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        taskItemRepository.delete(taskItem);
        log.info("Task deleted with id: {}", id);
    }

    private void mapRequestToEntity(TaskItem taskItem, TaskRequestDTO dto) {
        Employee assignedEmployee = employeeRepository.findById(dto.assignedEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + dto.assignedEmployeeId()));
        Project project = resolveProject(dto.projectId());

        taskItem.setTitle(dto.title());
        taskItem.setDescription(dto.description());
        taskItem.setStatus(dto.status());
        taskItem.setPriority(dto.priority());
        taskItem.setDueDate(dto.dueDate());
        taskItem.setAssignedEmployee(assignedEmployee);
        taskItem.setProject(project);
    }

    private Project resolveProject(Integer projectId) {
        if (projectId == null) {
            return null;
        }
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
    }

    private TaskResponseDTO toResponse(TaskItem taskItem) {
        return new TaskResponseDTO(
                taskItem.getId(),
                taskItem.getTitle(),
                taskItem.getDescription(),
                taskItem.getStatus(),
                taskItem.getPriority(),
                taskItem.getDueDate(),
                taskItem.getAssignedEmployee() != null ? taskItem.getAssignedEmployee().getId() : null,
                taskItem.getAssignedEmployee() != null ? taskItem.getAssignedEmployee().getName() : null,
                taskItem.getProject() != null ? taskItem.getProject().getId() : null,
                taskItem.getProject() != null ? taskItem.getProject().getTitle() : null
        );
    }
}
