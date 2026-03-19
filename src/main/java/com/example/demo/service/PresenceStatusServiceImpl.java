package com.example.demo.service;

import com.example.demo.dto.PresenceStatusRequestDTO;
import com.example.demo.dto.PresenceStatusResponseDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.model.PresenceStatus;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.PresenceStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PresenceStatusServiceImpl implements PresenceStatusService {

    private final PresenceStatusRepository presenceStatusRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public PresenceStatusResponseDTO save(PresenceStatusRequestDTO dto) {
        PresenceStatus presenceStatus = new PresenceStatus();
        mapRequestToEntity(presenceStatus, dto);
        PresenceStatus saved = presenceStatusRepository.save(presenceStatus);
        log.info("Presence status created with id: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PresenceStatusResponseDTO> getAll() {
        return presenceStatusRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PresenceStatusResponseDTO getById(int id) {
        PresenceStatus presenceStatus = presenceStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Presence status not found with id: " + id));
        return toResponse(presenceStatus);
    }

    @Override
    @Transactional
    public PresenceStatusResponseDTO update(int id, PresenceStatusRequestDTO dto) {
        PresenceStatus presenceStatus = presenceStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Presence status not found with id: " + id));
        mapRequestToEntity(presenceStatus, dto);
        PresenceStatus updated = presenceStatusRepository.save(presenceStatus);
        log.info("Presence status updated with id: {}", updated.getId());
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        PresenceStatus presenceStatus = presenceStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Presence status not found with id: " + id));
        presenceStatusRepository.delete(presenceStatus);
        log.info("Presence status deleted with id: {}", id);
    }

    private void mapRequestToEntity(PresenceStatus presenceStatus, PresenceStatusRequestDTO dto) {
        Employee employee = employeeRepository.findById(dto.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + dto.employeeId()));

        presenceStatus.setState(dto.state());
        presenceStatus.setLastSeenAt(dto.lastSeenAt() == null ? LocalDateTime.now() : dto.lastSeenAt());
        presenceStatus.setSource(dto.source());
        presenceStatus.setEmployee(employee);
    }

    private PresenceStatusResponseDTO toResponse(PresenceStatus presenceStatus) {
        return new PresenceStatusResponseDTO(
                presenceStatus.getId(),
                presenceStatus.getState(),
                presenceStatus.getLastSeenAt(),
                presenceStatus.getSource(),
                presenceStatus.getEmployee() != null ? presenceStatus.getEmployee().getId() : null,
                presenceStatus.getEmployee() != null ? presenceStatus.getEmployee().getName() : null
        );
    }
}
