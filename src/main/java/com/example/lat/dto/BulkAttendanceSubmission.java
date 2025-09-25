package com.example.lat.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class BulkAttendanceSubmission {
    private LocalDate date;
    private List<StudentAttendanceItem> attendanceItems;
    
    @Data
    public static class StudentAttendanceItem {
        private Long studentId;
        private boolean present;
    }
}