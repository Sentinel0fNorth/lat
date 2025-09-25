package com.example.attendanceapp.api

import com.example.attendanceapp.model.AttendanceRecord
import com.example.attendanceapp.model.AttendanceStatistics
import com.example.attendanceapp.model.BulkAttendanceSubmission
import com.example.attendanceapp.model.Student
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface AttendanceApi {

    // Student endpoints
    @GET("students")
    suspend fun getStudents(): Response<List<Student>>
    
    @GET("students")
    fun getStudentsSync(): Call<List<Student>>

    @GET("students/{id}")
    fun getStudentSync(@Path("id") id: Long): Call<Student>

    @POST("students")
    suspend fun createStudent(@Body student: Student): Response<Student>
    
    @POST("students")
    fun createStudentSync(@Body student: Student): Call<Student>

    @PUT("students/{id}")
    fun updateStudentSync(@Path("id") id: Long, @Body student: Student): Call<Student>

    @DELETE("students/{id}")
    fun deleteStudentSync(@Path("id") id: Long): Call<Void>

    // Attendance endpoints
    @POST("attendance")
    suspend fun submitAttendance(@Body record: AttendanceRecord): Response<AttendanceRecord>
    
    @POST("attendance")
    fun submitAttendanceSync(@Body record: AttendanceRecord): Call<AttendanceRecord>

    @POST("attendance/bulk")
    fun submitBulkAttendanceSync(@Body submission: BulkAttendanceSubmission): Call<List<AttendanceRecord>>

    @GET("attendance")
    suspend fun getAttendanceByDate(@Query("date") date: String): Response<List<AttendanceRecord>>
    
    @GET("attendance")
    fun getAttendanceByDateSync(@Query("date") date: String): Call<List<AttendanceRecord>>

    @GET("attendance/student/{studentId}")
    fun getAttendanceByStudentSync(@Path("studentId") studentId: Long): Call<List<AttendanceRecord>>

    @PUT("attendance/{id}")
    fun updateAttendanceSync(@Path("id") id: Long, @Body record: AttendanceRecord): Call<AttendanceRecord>

    @DELETE("attendance/{id}")
    fun deleteAttendanceSync(@Path("id") id: Long): Call<Void>

    // Statistics endpoints
    @GET("attendance/statistics")
    fun getAttendanceStatisticsSync(): Call<List<AttendanceStatistics>>

    @GET("attendance/statistics/low-attendance")
    fun getLowAttendanceStudentsSync(@Query("threshold") threshold: Double = 75.0): Call<List<AttendanceStatistics>>
}
