package com.example.lat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lat.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {}

