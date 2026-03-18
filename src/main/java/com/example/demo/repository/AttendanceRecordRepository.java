package com.example.demo.repository;

import com.example.demo.model.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Integer> {

    Optional<AttendanceRecord> findByEmployeeIdAndWorkDate(Integer employeeId, LocalDate workDate);
}
