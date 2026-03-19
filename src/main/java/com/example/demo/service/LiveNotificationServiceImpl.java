package com.example.demo.service;

import com.example.demo.dto.LiveNotificationRequestDTO;
import com.example.demo.dto.LiveNotificationResponseDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.model.LiveNotification;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.LiveNotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LiveNotificationServiceImpl implements LiveNotificationService {

    private final LiveNotificationRepository liveNotificationRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public LiveNotificationResponseDTO save(LiveNotificationRequestDTO dto) {
        LiveNotification notification = new LiveNotification();
        mapRequestToEntity(notification, dto);
        LiveNotification saved = liveNotificationRepository.save(notification);
        log.info("Live notification created with id: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LiveNotificationResponseDTO> getAll() {
        return liveNotificationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LiveNotificationResponseDTO getById(int id) {
        LiveNotification notification = liveNotificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Live notification not found with id: " + id));
        return toResponse(notification);
    }

    @Override
    @Transactional
    public LiveNotificationResponseDTO update(int id, LiveNotificationRequestDTO dto) {
        LiveNotification notification = liveNotificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Live notification not found with id: " + id));
        mapRequestToEntity(notification, dto);
        LiveNotification updated = liveNotificationRepository.save(notification);
        log.info("Live notification updated with id: {}", updated.getId());
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        LiveNotification notification = liveNotificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Live notification not found with id: " + id));
        liveNotificationRepository.delete(notification);
        log.info("Live notification deleted with id: {}", id);
    }

    private void mapRequestToEntity(LiveNotification notification, LiveNotificationRequestDTO dto) {
        Employee employee = employeeRepository.findById(dto.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + dto.employeeId()));

        notification.setMessage(dto.message());
        notification.setType(dto.type());
        notification.setReadFlag(dto.readFlag());
        notification.setDeliveredAt(dto.deliveredAt() == null ? LocalDateTime.now() : dto.deliveredAt());
        notification.setEmployee(employee);
    }

    private LiveNotificationResponseDTO toResponse(LiveNotification notification) {
        return new LiveNotificationResponseDTO(
                notification.getId(),
                notification.getMessage(),
                notification.getType(),
                notification.getReadFlag(),
                notification.getDeliveredAt(),
                notification.getEmployee() != null ? notification.getEmployee().getId() : null,
                notification.getEmployee() != null ? notification.getEmployee().getName() : null
        );
    }
}
