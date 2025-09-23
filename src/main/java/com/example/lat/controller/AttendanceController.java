package com.example.lat.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.lat.model.AttendanceRecord;
import com.example.lat.service.AttendanceService;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {
  @Autowired private AttendanceService service;

  @PostMapping
  public AttendanceRecord mark(@RequestBody AttendanceRecord record) {
    return service.markAttendance(record);
  }

  @GetMapping
  public List<AttendanceRecord> getByDate(@RequestParam LocalDate date) {
    return service.getAttendanceByDate(date);
  }
}


