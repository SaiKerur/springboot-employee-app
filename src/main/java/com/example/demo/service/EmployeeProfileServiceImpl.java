package com.example.demo.service;

import com.example.demo.dto.EmployeeProfileRequestDTO;
import com.example.demo.dto.EmployeeProfileResponseDTO;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.model.EmployeeProfile;
import com.example.demo.repository.EmployeeProfileRepository;
import com.example.demo.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeProfileServiceImpl implements EmployeeProfileService {

    private final EmployeeProfileRepository employeeProfileRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public EmployeeProfileResponseDTO save(EmployeeProfileRequestDTO dto) {
        employeeProfileRepository.findByEmployeeId(dto.employeeId()).ifPresent(existing -> {
            throw new DuplicateResourceException("Employee profile already exists for employee id: " + dto.employeeId());
        });

        Employee employee = findEmployee(dto.employeeId());

        EmployeeProfile profile = new EmployeeProfile();
        profile.setPhoneNumber(dto.phoneNumber());
        profile.setAddress(dto.address());
        profile.setEmergencyContact(dto.emergencyContact());
        profile.setEmployee(employee);
        employee.assignProfile(profile);

        EmployeeProfile saved = employeeProfileRepository.save(profile);
        log.info("Employee profile created with id: {}", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeProfileResponseDTO> getAll() {
        return employeeProfileRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeProfileResponseDTO getById(int id) {
        EmployeeProfile profile = employeeProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee profile not found with id: " + id));
        return toResponse(profile);
    }

    @Override
    @Transactional
    public EmployeeProfileResponseDTO update(int id, EmployeeProfileRequestDTO dto) {
        EmployeeProfile profile = employeeProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee profile not found with id: " + id));

        Employee employee = findEmployee(dto.employeeId());
        Employee previousEmployee = profile.getEmployee();
        employeeProfileRepository.findByEmployeeId(dto.employeeId())
                .filter(existing -> existing.getId() != id)
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Employee profile already exists for employee id: " + dto.employeeId());
                });

        profile.setPhoneNumber(dto.phoneNumber());
        profile.setAddress(dto.address());
        profile.setEmergencyContact(dto.emergencyContact());
        if (previousEmployee != null && previousEmployee.getId() != employee.getId()) {
            previousEmployee.setProfile(null);
        }
        profile.setEmployee(employee);
        employee.assignProfile(profile);

        EmployeeProfile updated = employeeProfileRepository.save(profile);
        log.info("Employee profile updated with id: {}", updated.getId());
        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        EmployeeProfile profile = employeeProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee profile not found with id: " + id));
        Employee employee = profile.getEmployee();
        if (employee != null) {
            employee.setProfile(null);
        }
        employeeProfileRepository.delete(profile);
        log.info("Employee profile deleted with id: {}", id);
    }

    private Employee findEmployee(int employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
    }

    private EmployeeProfileResponseDTO toResponse(EmployeeProfile profile) {
        return new EmployeeProfileResponseDTO(
                profile.getId(),
                profile.getPhoneNumber(),
                profile.getAddress(),
                profile.getEmergencyContact(),
                profile.getEmployee().getId(),
                profile.getEmployee().getName()
        );
    }
}
