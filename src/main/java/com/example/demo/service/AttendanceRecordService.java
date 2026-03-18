package com.example.demo.service;

import com.example.demo.dto.AttendanceRequestDTO;
import com.example.demo.dto.AttendanceResponseDTO;

import java.util.List;

public interface AttendanceRecordService {

    AttendanceResponseDTO save(AttendanceRequestDTO attendanceRecord);

    List<AttendanceResponseDTO> getAll();

    AttendanceResponseDTO getById(int id);

    AttendanceResponseDTO update(int id, AttendanceRequestDTO attendanceRecord);

    void deleteById(int id);
}
