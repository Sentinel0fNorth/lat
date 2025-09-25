package com.example.lat.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

  @GetMapping("/{id}")
  public ResponseEntity<Student> getById(@PathVariable Long id) {
    Optional<Student> student = service.getStudentById(id);
    return student.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Student create(@RequestBody Student student) {
    return service.saveStudent(student);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Student> update(@PathVariable Long id, @RequestBody Student student) {
    if (!service.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    student.setId(id);
    return ResponseEntity.ok(service.saveStudent(student));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    if (!service.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    service.deleteStudent(id);
    return ResponseEntity.noContent().build();
  }
}

