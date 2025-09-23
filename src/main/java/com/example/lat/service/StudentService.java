package com.example.lat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lat.model.Student;
import com.example.lat.repository.StudentRepository;

@Service
public class StudentService {
  @Autowired private StudentRepository repo;

  public List<Student> getAllStudents() {
    return repo.findAll();
  }

  public Student saveStudent(Student student) {
    return repo.save(student);
  }
}

