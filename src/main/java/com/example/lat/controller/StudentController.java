package com.example.lat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lat.model.Student;
import com.example.lat.service.StudentService;

@RestController
@RequestMapping("/students")
public class StudentController {
  @Autowired private StudentService service;

  @GetMapping
  public List<Student> getAll() {
    return service.getAllStudents();
  }

  @PostMapping
  public Student create(@RequestBody Student student) {
    return service.saveStudent(student);
  }
}

