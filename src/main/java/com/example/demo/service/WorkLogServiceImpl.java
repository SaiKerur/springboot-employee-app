package com.example.demo.service;

import com.example.demo.dto.WorkLogRequestDTO;
import com.example.demo.dto.WorkLogResponseDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.model.WorkLog;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.WorkLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkLogServiceImpl implements WorkLogService {

    private final WorkLogRepository workLogRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public WorkLogResponseDTO save(WorkLogRequestDTO dto) {
        WorkLog workLog = new WorkLog();
        mapRequestToEntity(workLog, dto);
        WorkLog saved = workLogRepository.save(workLog);
        log.info("Work log created with id: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkLogResponseDTO> getAll() {
        return workLogRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public WorkLogResponseDTO getById(int id) {
        WorkLog workLog = workLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work log not found with id: " + id));
        return toResponse(workLog);
    }

    @Override
    @Transactional
    public WorkLogResponseDTO update(int id, WorkLogRequestDTO dto) {
        WorkLog workLog = workLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work log not found with id: " + id));
        mapRequestToEntity(workLog, dto);
        WorkLog updated = workLogRepository.save(workLog);
        log.info("Work log updated with id: {}", updated.getId());
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        WorkLog workLog = workLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work log not found with id: " + id));
        workLogRepository.delete(workLog);
        log.info("Work log deleted with id: {}", id);
    }

    private void mapRequestToEntity(WorkLog workLog, WorkLogRequestDTO dto) {
        Employee employee = employeeRepository.findById(dto.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + dto.employeeId()));

        workLog.setActivity(dto.activity());
        workLog.setDurationMinutes(dto.durationMinutes());
        workLog.setWorkDate(dto.workDate());
        workLog.setEmployee(employee);
    }

    private WorkLogResponseDTO toResponse(WorkLog workLog) {
        return new WorkLogResponseDTO(
                workLog.getId(),
                workLog.getActivity(),
                workLog.getDurationMinutes(),
                workLog.getWorkDate(),
                workLog.getEmployee() != null ? workLog.getEmployee().getId() : null,
                workLog.getEmployee() != null ? workLog.getEmployee().getName() : null
        );
    }
}
