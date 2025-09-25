package com.example.attendanceapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendanceapp.api.ApiClient
import com.example.attendanceapp.api.AttendanceApi
import com.example.attendanceapp.databinding.ActivityStudentManagementBinding
import com.example.attendanceapp.model.AttendanceStatistics
import com.example.attendanceapp.model.Student
import com.example.attendanceapp.ui.adapter.StudentManagementAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentManagementBinding
    private lateinit var adapter: StudentManagementAdapter
    private val students = mutableListOf<Student>()
    private val statistics = mutableListOf<AttendanceStatistics>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        loadData()
    }

    private fun setupUI() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Manage Students"

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.fabAddStudent.setOnClickListener {
            showAddStudentDialog()
        }
    }

    private fun loadData() {
        showLoading(true)
        
        // Load students first
        val api = ApiClient.getClient().create(AttendanceApi::class.java)
        api.getStudentsSync().enqueue(object : Callback<List<Student>> {
            override fun onResponse(call: Call<List<Student>>, response: Response<List<Student>>) {
                if (response.isSuccessful) {
                    students.clear()
                    response.body()?.let { students.addAll(it) }
                    
                    // Now load statistics
                    loadStatistics()
                } else {
                    showLoading(false)
                    showError("Failed to load students: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                showLoading(false)
                showError("Network error: ${t.message}")
            }
        })
    }

    private fun loadStatistics() {
        val api = ApiClient.getClient().create(AttendanceApi::class.java)
        api.getAttendanceStatisticsSync().enqueue(object : Callback<List<AttendanceStatistics>> {
            override fun onResponse(call: Call<List<AttendanceStatistics>>, response: Response<List<AttendanceStatistics>>) {
                showLoading(false)
                
                if (response.isSuccessful) {
                    statistics.clear()
                    response.body()?.let { statistics.addAll(it) }
                    
                    setupAdapter()
                    
                    if (students.isEmpty()) {
                        showEmptyState()
                    } else {
                        hideEmptyState()
                    }
                } else {
                    showError("Failed to load statistics: ${response.code()}")
                    setupAdapter() // Setup adapter anyway with empty statistics
                }
            }

            override fun onFailure(call: Call<List<AttendanceStatistics>>, t: Throwable) {
                showLoading(false)
                showError("Network error loading statistics: ${t.message}")
                setupAdapter() // Setup adapter anyway with empty statistics
            }
        })
    }

    private fun setupAdapter() {
        adapter = StudentManagementAdapter(
            students,
            statistics,
            onEditClick = { student -> showEditStudentDialog(student) },
            onDeleteClick = { student -> showDeleteConfirmation(student) }
        )
        binding.recyclerView.adapter = adapter
    }

    private fun showAddStudentDialog() {
        val dialog = StudentDialogHelper.createAddStudentDialog(this) { name, rollNumber ->
            createStudent(Student(null, name, rollNumber))
        }
        dialog.show()
    }

    private fun showEditStudentDialog(student: Student) {
        val dialog = StudentDialogHelper.createEditStudentDialog(this, student) { name, rollNumber ->
            updateStudent(student.copy(name = name, rollNumber = rollNumber))
        }
        dialog.show()
    }

    private fun createStudent(student: Student) {
        val api = ApiClient.getClient().create(AttendanceApi::class.java)
        api.createStudentSync(student).enqueue(object : Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if (response.isSuccessful) {
                    response.body()?.let { createdStudent ->
                        students.add(createdStudent)
                        adapter.notifyDataSetChanged()
                        hideEmptyState()
                        Toast.makeText(this@StudentManagementActivity, "Student added successfully!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    showError("Failed to add student: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                showError("Network error: ${t.message}")
            }
        })
    }

    private fun updateStudent(student: Student) {
        student.id?.let { id ->
            val api = ApiClient.getClient().create(AttendanceApi::class.java)
            api.updateStudentSync(id, student).enqueue(object : Callback<Student> {
                override fun onResponse(call: Call<Student>, response: Response<Student>) {
                    if (response.isSuccessful) {
                        response.body()?.let { updatedStudent ->
                            val index = students.indexOfFirst { it.id == updatedStudent.id }
                            if (index != -1) {
                                students[index] = updatedStudent
                                adapter.notifyDataSetChanged()
                                Toast.makeText(this@StudentManagementActivity, "Student updated successfully!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        showError("Failed to update student: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Student>, t: Throwable) {
                    showError("Network error: ${t.message}")
                }
            })
        }
    }

    private fun showDeleteConfirmation(student: Student) {
        AlertDialog.Builder(this)
            .setTitle("Delete Student")
            .setMessage("Are you sure you want to delete ${student.name}? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                deleteStudent(student)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteStudent(student: Student) {
        student.id?.let { id ->
            val api = ApiClient.getClient().create(AttendanceApi::class.java)
            api.deleteStudentSync(id).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        val index = students.indexOfFirst { it.id == student.id }
                        if (index != -1) {
                            students.removeAt(index)
                            adapter.notifyDataSetChanged()
                            
                            if (students.isEmpty()) {
                                showEmptyState()
                            }
                            
                            Toast.makeText(this@StudentManagementActivity, "Student deleted successfully!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        showError("Failed to delete student: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    showError("Network error: ${t.message}")
                }
            })
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.fabAddStudent.isEnabled = !isLoading
    }

    private fun showEmptyState() {
        binding.emptyStateLayout.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    private fun hideEmptyState() {
        binding.emptyStateLayout.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_student_management, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_refresh -> {
                loadData()
                true
            }
            R.id.action_low_attendance -> {
                Toast.makeText(this, "Low attendance report - Feature coming soon!", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}