package com.example.attendanceapp.model

data class BulkAttendanceSubmission(
    val date: String, // yyyy-MM-dd format
    val attendanceItems: List<StudentAttendanceItem>
)

data class StudentAttendanceItem(
    val studentId: Long,
    val present: Boolean
)