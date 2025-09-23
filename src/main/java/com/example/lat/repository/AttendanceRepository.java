package com.example.lat.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lat.model.AttendanceRecord;

public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {
  List<AttendanceRecord> findByDate(LocalDate date);
}

