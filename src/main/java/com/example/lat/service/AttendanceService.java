package com.example.lat.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lat.dto.AttendanceStatistics;
import com.example.lat.dto.BulkAttendanceSubmission;
import com.example.lat.model.AttendanceRecord;
import com.example.lat.model.Student;
import com.example.lat.repository.AttendanceRepository;
import com.example.lat.repository.StudentRepository;

@Service
public class AttendanceService {
  @Autowired private AttendanceRepository repo;
  @Autowired private StudentRepository studentRepo;

  public AttendanceRecord markAttendance(AttendanceRecord record) {
    return repo.save(record);
  }

  public List<AttendanceRecord> markBulkAttendance(BulkAttendanceSubmission submission) {
    List<AttendanceRecord> records = new ArrayList<>();
    
    for (BulkAttendanceSubmission.StudentAttendanceItem item : submission.getAttendanceItems()) {
      Optional<Student> student = studentRepo.findById(item.getStudentId());
      if (student.isPresent()) {
        // Check if attendance already exists for this student and date
        List<AttendanceRecord> existing = repo.findByStudentAndDate(student.get(), submission.getDate());
        
        AttendanceRecord record;
        if (!existing.isEmpty()) {
          // Update existing record
          record = existing.get(0);
          record.setStatus(item.isPresent() ? AttendanceRecord.Status.PRESENT : AttendanceRecord.Status.ABSENT);
        } else {
          // Create new record
          record = new AttendanceRecord();
          record.setStudent(student.get());
          record.setDate(submission.getDate());
          record.setStatus(item.isPresent() ? AttendanceRecord.Status.PRESENT : AttendanceRecord.Status.ABSENT);
        }
        records.add(repo.save(record));
      }
    }
    
    return records;
  }

  public List<AttendanceRecord> getAttendanceByDate(LocalDate date) {
    return repo.findByDate(date);
  }

  public List<AttendanceRecord> getAttendanceByStudent(Long studentId) {
    Optional<Student> student = studentRepo.findById(studentId);
    return student.map(repo::findByStudent).orElse(new ArrayList<>());
  }

  public List<AttendanceStatistics> getAttendanceStatistics() {
    List<Student> students = studentRepo.findAll();
    return students.stream()
        .map(this::calculateAttendanceStatistics)
        .collect(Collectors.toList());
  }

  public List<AttendanceStatistics> getLowAttendanceStudents(double threshold) {
    return getAttendanceStatistics().stream()
        .filter(stats -> stats.getAttendancePercentage() < threshold)
        .collect(Collectors.toList());
  }

  public AttendanceRecord updateAttendance(Long id, AttendanceRecord record) {
    Optional<AttendanceRecord> existing = repo.findById(id);
    if (existing.isPresent()) {
      AttendanceRecord existingRecord = existing.get();
      existingRecord.setStatus(record.getStatus());
      existingRecord.setDate(record.getDate());
      return repo.save(existingRecord);
    }
    return null;
  }

  public boolean deleteAttendance(Long id) {
    if (repo.existsById(id)) {
      repo.deleteById(id);
      return true;
    }
    return false;
  }

  private AttendanceStatistics calculateAttendanceStatistics(Student student) {
    List<AttendanceRecord> records = repo.findByStudent(student);
    long totalDays = records.size();
    long presentDays = records.stream()
        .mapToLong(record -> record.getStatus() == AttendanceRecord.Status.PRESENT ? 1 : 0)
        .sum();
    
    return new AttendanceStatistics(
        student.getId(),
        student.getName(),
        student.getRollNumber(),
        totalDays,
        presentDays
    );
  }
}

