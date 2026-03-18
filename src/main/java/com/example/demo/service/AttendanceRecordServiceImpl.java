package com.example.demo.service;

import com.example.demo.dto.AttendanceRequestDTO;
import com.example.demo.dto.AttendanceResponseDTO;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.AttendanceRecord;
import com.example.demo.model.Employee;
import com.example.demo.repository.AttendanceRecordRepository;
import com.example.demo.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceRecordServiceImpl implements AttendanceRecordService {

    private final AttendanceRecordRepository attendanceRecordRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public AttendanceResponseDTO save(AttendanceRequestDTO dto) {
        attendanceRecordRepository.findByEmployeeIdAndWorkDate(dto.employeeId(), dto.workDate())
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Attendance already exists for employee "
                            + dto.employeeId() + " on date " + dto.workDate());
                });

        AttendanceRecord attendanceRecord = new AttendanceRecord();
        mapRequestToEntity(attendanceRecord, dto);
        AttendanceRecord saved = attendanceRecordRepository.save(attendanceRecord);
        log.info("Attendance record created with id: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponseDTO> getAll() {
        return attendanceRecordRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceResponseDTO getById(int id) {
        AttendanceRecord attendanceRecord = attendanceRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with id: " + id));
        return toResponse(attendanceRecord);
    }

    @Override
    @Transactional
    public AttendanceResponseDTO update(int id, AttendanceRequestDTO dto) {
        AttendanceRecord attendanceRecord = attendanceRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with id: " + id));

        attendanceRecordRepository.findByEmployeeIdAndWorkDate(dto.employeeId(), dto.workDate())
                .filter(existing -> existing.getId() != id)
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Attendance already exists for employee "
                            + dto.employeeId() + " on date " + dto.workDate());
                });

        mapRequestToEntity(attendanceRecord, dto);
        AttendanceRecord updated = attendanceRecordRepository.save(attendanceRecord);
        log.info("Attendance record updated with id: {}", updated.getId());
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        AttendanceRecord attendanceRecord = attendanceRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with id: " + id));
        attendanceRecordRepository.delete(attendanceRecord);
        log.info("Attendance record deleted with id: {}", id);
    }

    private void mapRequestToEntity(AttendanceRecord attendanceRecord, AttendanceRequestDTO dto) {
        Employee employee = employeeRepository.findById(dto.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + dto.employeeId()));
        validateShiftWindow(dto);

        attendanceRecord.setEmployee(employee);
        attendanceRecord.setWorkDate(dto.workDate());
        attendanceRecord.setCheckInAt(dto.checkInAt());
        attendanceRecord.setCheckOutAt(dto.checkOutAt());
        attendanceRecord.setStatus(dto.status());
    }

    private void validateShiftWindow(AttendanceRequestDTO dto) {
        if (dto.checkInAt() != null && dto.checkOutAt() != null && dto.checkOutAt().isBefore(dto.checkInAt())) {
            throw new IllegalArgumentException("Check-out time cannot be before check-in time");
        }
    }

    private AttendanceResponseDTO toResponse(AttendanceRecord attendanceRecord) {
        return new AttendanceResponseDTO(
                attendanceRecord.getId(),
                attendanceRecord.getEmployee() != null ? attendanceRecord.getEmployee().getId() : null,
                attendanceRecord.getEmployee() != null ? attendanceRecord.getEmployee().getName() : null,
                attendanceRecord.getWorkDate(),
                attendanceRecord.getCheckInAt(),
                attendanceRecord.getCheckOutAt(),
                attendanceRecord.getStatus()
        );
    }
}
