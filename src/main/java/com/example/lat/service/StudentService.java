package com.example.lat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lat.model.Student;
import com.example.lat.repository.AttendanceRepository;
import com.example.lat.repository.StudentRepository;

@Service
public class StudentService {
  @Autowired private StudentRepository repo;
  @Autowired private AttendanceRepository attendanceRepo;

  public List<Student> getAllStudents() {
    return repo.findAll();
  }

  public Optional<Student> getStudentById(Long id) {
    return repo.findById(id);
  }

  public Student saveStudent(Student student) {
    return repo.save(student);
  }

  public boolean existsById(Long id) {
    return repo.existsById(id);
  }

  @Transactional
  public void deleteStudent(Long id) {
    // First delete all attendance records for this student
    attendanceRepo.deleteByStudentId(id);
    // Then delete the student
    repo.deleteById(id);
  }
}

