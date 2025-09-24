package com.example.attendanceapp.api

import com.example.attendanceapp.model.AttendanceRecord
import com.example.attendanceapp.model.Student
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AttendanceApi {

    @GET("students")
    suspend fun getStudents(): Response<List<Student>>
    
    @GET("students")
    fun getStudentsSync(): Call<List<Student>>

    @POST("students")
    suspend fun createStudent(@Body student: Student): Response<Student>
    
    @POST("students")
    fun createStudentSync(@Body student: Student): Call<Student>

    @POST("attendance")
    suspend fun submitAttendance(@Body record: AttendanceRecord): Response<AttendanceRecord>
    
    @POST("attendance")
    fun submitAttendanceSync(@Body record: AttendanceRecord): Call<AttendanceRecord>

    @GET("attendance")
    suspend fun getAttendanceByDate(@Query("date") date: String): Response<List<AttendanceRecord>>
    
    @GET("attendance")
    fun getAttendanceByDateSync(@Query("date") date: String): Call<List<AttendanceRecord>>
}
