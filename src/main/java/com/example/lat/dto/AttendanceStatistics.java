package com.example.lat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceStatistics {
    private Long studentId;
    private String studentName;
    private String rollNumber;
    private long totalDays;
    private long presentDays;
    private double attendancePercentage;
    
    public AttendanceStatistics(Long studentId, String studentName, String rollNumber, long totalDays, long presentDays) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.rollNumber = rollNumber;
        this.totalDays = totalDays;
        this.presentDays = presentDays;
        this.attendancePercentage = totalDays > 0 ? (presentDays * 100.0 / totalDays) : 0.0;
    }
}