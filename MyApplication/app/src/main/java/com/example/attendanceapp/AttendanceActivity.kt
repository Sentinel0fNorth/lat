package com.example.attendanceapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendanceapp.api.ApiClient
import com.example.attendanceapp.api.AttendanceApi
import com.example.attendanceapp.databinding.ActivityAttendanceBinding
import com.example.attendanceapp.model.AttendanceRecord
import com.example.attendanceapp.model.AttendanceStatus
import com.example.attendanceapp.model.Student
import com.example.attendanceapp.ui.adapter.StudentAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AttendanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAttendanceBinding
    private lateinit var adapter: StudentAdapter
    // No token needed since we disabled authentication
    private var selectedDate = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityAttendanceBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setupUI()
            loadStudents()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun setupUI() {
        // Setup toolbar
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Take Attendance"
        
        // Set current date
        updateDateDisplay()
        
        // Setup RecyclerView
        binding.studentRecyclerView.layoutManager = LinearLayoutManager(this)
        
        // Setup button listeners
        binding.changeDateButton.setOnClickListener {
            showDatePicker()
        }
        
        binding.selectAllButton.setOnClickListener {
            if (::adapter.isInitialized) {
                adapter.selectAll()
            }
        }
        
        binding.clearAllButton.setOnClickListener {
            if (::adapter.isInitialized) {
                adapter.clearAll()
            }
        }
        
        binding.submitButton.setOnClickListener {
            submitAttendance()
        }
    }
    
    private fun updateDateDisplay() {
        binding.dateTextView.text = displayDateFormat.format(selectedDate.time)
    }
    
    private fun showDatePicker() {
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedDate.set(year, month, dayOfMonth)
                updateDateDisplay()
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun loadStudents() {
        showLoading(true)
        
        // For testing purposes, let's try to load from API but fallback to dummy data
        try {
            val api = ApiClient.getClient().create(AttendanceApi::class.java)
            api.getStudentsSync().enqueue(object : Callback<List<Student>> {
                override fun onResponse(call: Call<List<Student>>, response: Response<List<Student>>) {
                    showLoading(false)
                    
                    if (response.isSuccessful) {
                        val students = response.body() ?: emptyList()
                        if (students.isNotEmpty()) {
                            adapter = StudentAdapter(students)
                            binding.studentRecyclerView.adapter = adapter
                            // Update toolbar subtitle with student count
                            supportActionBar?.subtitle = "${students.size} students"
                        } else {
                            loadDummyStudents()
                        }
                    } else {
                        loadDummyStudents()
                    }
                }

                override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                    showLoading(false)
                    loadDummyStudents()
                }
            })
        } catch (e: Exception) {
            showLoading(false)
            loadDummyStudents()
        }
    }
    
    private fun loadDummyStudents() {
        // Create some dummy student data for testing
        val dummyStudents = listOf(
            Student(1L, "John Doe", "CS101"),
            Student(2L, "Jane Smith", "CS102"),
            Student(3L, "Bob Johnson", "CS103"),
            Student(4L, "Alice Brown", "CS104"),
            Student(5L, "Charlie Wilson", "CS105"),
            Student(6L, "Diana Davis", "CS106"),
            Student(7L, "Eve Miller", "CS107"),
            Student(8L, "Frank Taylor", "CS108")
        )
        
        adapter = StudentAdapter(dummyStudents)
        binding.studentRecyclerView.adapter = adapter
        
        // Update toolbar subtitle with student count
        supportActionBar?.subtitle = "${dummyStudents.size} demo students"
        
        Toast.makeText(this, "Loaded ${dummyStudents.size} demo students (backend offline)", Toast.LENGTH_SHORT).show()
    }
    
    private fun submitAttendance() {
        if (!::adapter.isInitialized) {
            Toast.makeText(this, "No students loaded", Toast.LENGTH_SHORT).show()
            return
        }
        
        showLoading(true)
        
        val attendanceMap = adapter.getAttendanceMap()
        val presentCount = attendanceMap.values.count { it }
        
        // Create bulk attendance submission
        val attendanceItems = attendanceMap.map { (studentId, isPresent) ->
            com.example.attendanceapp.model.StudentAttendanceItem(studentId, isPresent)
        }
        
        val bulkSubmission = com.example.attendanceapp.model.BulkAttendanceSubmission(
            date = dateFormat.format(selectedDate.time),
            attendanceItems = attendanceItems
        )
        
        val api = ApiClient.getClient().create(AttendanceApi::class.java)
        api.submitBulkAttendanceSync(bulkSubmission).enqueue(object : Callback<List<com.example.attendanceapp.model.AttendanceRecord>> {
            override fun onResponse(call: Call<List<com.example.attendanceapp.model.AttendanceRecord>>, response: Response<List<com.example.attendanceapp.model.AttendanceRecord>>) {
                showLoading(false)
                
                if (response.isSuccessful) {
                    val records = response.body()
                    Toast.makeText(
                        this@AttendanceActivity,
                        "Attendance submitted successfully!\n$presentCount students marked present\n${records?.size ?: 0} records created",
                        Toast.LENGTH_LONG
                    ).show()
                    
                    // Clear selections after successful submission
                    adapter.clearAll()
                } else {
                    Toast.makeText(
                        this@AttendanceActivity,
                        "Failed to submit attendance: ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<com.example.attendanceapp.model.AttendanceRecord>>, t: Throwable) {
                showLoading(false)
                Toast.makeText(
                    this@AttendanceActivity,
                    "Network error: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
    
    private fun showLoading(isLoading: Boolean) {
        binding.loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.submitButton.isEnabled = !isLoading
        binding.selectAllButton.isEnabled = !isLoading
        binding.clearAllButton.isEnabled = !isLoading
        binding.changeDateButton.isEnabled = !isLoading
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_attendance, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                refreshStudentList()
                true
            }
            R.id.action_manage_students -> {
                val intent = Intent(this, StudentManagementActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_history -> {
                val intent = Intent(this, AttendanceHistoryActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_low_attendance -> {
                val intent = Intent(this, LowAttendanceReportActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    
    private fun refreshStudentList() {
        Toast.makeText(this, "Refreshing student list...", Toast.LENGTH_SHORT).show()
        loadStudents()
    }
    
    override fun onResume() {
        super.onResume()
        // Auto-refresh the student list when returning from other activities
        // This ensures the list is always up-to-date
        if (::adapter.isInitialized) {
            loadStudents()
        }
    }
}
