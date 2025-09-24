package com.example.attendanceapp.model

import java.time.LocalDate

data class AttendanceRecord(
    val id: Long? = null,
    val student: Student,
    val date: String, // We'll send as string to match backend LocalDate
    val status: AttendanceStatus
)

enum class AttendanceStatus {
    PRESENT, ABSENT
}

// For sending attendance data to backend
data class AttendanceSubmission(
    val date: String,
    val records: List<AttendanceRecord>
)
