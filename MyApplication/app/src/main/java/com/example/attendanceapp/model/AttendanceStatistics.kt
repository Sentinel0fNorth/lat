package com.example.attendanceapp.model

data class AttendanceStatistics(
    val studentId: Long,
    val studentName: String,
    val rollNumber: String,
    val totalDays: Long,
    val presentDays: Long,
    val attendancePercentage: Double
)