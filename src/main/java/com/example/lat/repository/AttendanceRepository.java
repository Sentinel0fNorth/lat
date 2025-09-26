package com.example.lat.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.lat.model.AttendanceRecord;
import com.example.lat.model.Student;

public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {
  List<AttendanceRecord> findByDate(LocalDate date);
  List<AttendanceRecord> findByStudent(Student student);
  List<AttendanceRecord> findByStudentAndDate(Student student, LocalDate date);
  
  @Modifying
  void deleteByStudent(Student student);
  
  @Modifying
  @Query("DELETE FROM AttendanceRecord a WHERE a.student.id = :studentId")
  void deleteByStudentId(@Param("studentId") Long studentId);
}

