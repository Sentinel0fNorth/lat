package com.example.lat.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.lat.dto.AttendanceStatistics;
import com.example.lat.dto.BulkAttendanceSubmission;
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

  @PostMapping("/bulk")
  public ResponseEntity<List<AttendanceRecord>> markBulkAttendance(@RequestBody BulkAttendanceSubmission submission) {
    List<AttendanceRecord> records = service.markBulkAttendance(submission);
    return ResponseEntity.ok(records);
  }

  @GetMapping
  public List<AttendanceRecord> getByDate(@RequestParam LocalDate date) {
    return service.getAttendanceByDate(date);
  }

  @GetMapping("/student/{studentId}")
  public List<AttendanceRecord> getByStudent(@PathVariable Long studentId) {
    return service.getAttendanceByStudent(studentId);
  }

  @GetMapping("/statistics")
  public List<AttendanceStatistics> getAttendanceStatistics() {
    return service.getAttendanceStatistics();
  }

  @GetMapping("/statistics/low-attendance")
  public List<AttendanceStatistics> getLowAttendanceStudents(@RequestParam(defaultValue = "75.0") double threshold) {
    return service.getLowAttendanceStudents(threshold);
  }

  @PutMapping("/{id}")
  public ResponseEntity<AttendanceRecord> updateAttendance(@PathVariable Long id, @RequestBody AttendanceRecord record) {
    AttendanceRecord updated = service.updateAttendance(id, record);
    return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAttendance(@PathVariable Long id) {
    boolean deleted = service.deleteAttendance(id);
    return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }
}


