package com.example.lat.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lat.model.AttendanceRecord;
import com.example.lat.repository.AttendanceRepository;

@Service
public class AttendanceService {
  @Autowired private AttendanceRepository repo;

  public AttendanceRecord markAttendance(AttendanceRecord record) {
    return repo.save(record);
  }

  public List<AttendanceRecord> getAttendanceByDate(LocalDate date) {
    return repo.findByDate(date);
  }
}

