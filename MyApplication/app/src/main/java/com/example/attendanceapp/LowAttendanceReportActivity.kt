package com.example.attendanceapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.attendanceapp.adapter.LowAttendanceReportAdapter
import com.example.attendanceapp.api.ApiClient
import com.example.attendanceapp.api.AttendanceApi
import com.example.attendanceapp.model.AttendanceStatistics
import com.example.attendanceapp.utils.ExportUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LowAttendanceReportActivity : AppCompatActivity() {
    
    private lateinit var thresholdEditText: EditText
    private lateinit var loadButton: Button
    private lateinit var exportPdfButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var statusTextView: TextView
    private lateinit var loadingProgressBar: ProgressBar
    
    private lateinit var adapter: LowAttendanceReportAdapter
    private var currentStudents: List<AttendanceStatistics> = emptyList()
    private var currentThreshold: Double = 75.0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_low_attendance_report)
        
        supportActionBar?.title = "Low Attendance Report"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        initializeViews()
        setupRecyclerView()
        setupListeners()
        
        // Load initial data with default threshold
        loadLowAttendanceStudents(currentThreshold)
    }
    
    private fun initializeViews() {
        thresholdEditText = findViewById(R.id.thresholdEditText)
        loadButton = findViewById(R.id.loadButton)
        exportPdfButton = findViewById(R.id.exportPdfButton)
        recyclerView = findViewById(R.id.recyclerView)
        statusTextView = findViewById(R.id.statusTextView)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        
        thresholdEditText.setText(currentThreshold.toString())
    }
    
    private fun setupRecyclerView() {
        adapter = LowAttendanceReportAdapter(currentStudents)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
    
    private fun setupListeners() {
        loadButton.setOnClickListener {
            val thresholdText = thresholdEditText.text.toString().trim()
            if (thresholdText.isEmpty()) {
                thresholdEditText.error = "Please enter a threshold"
                return@setOnClickListener
            }
            
            val threshold = thresholdText.toDoubleOrNull()
            if (threshold == null || threshold < 0 || threshold > 100) {
                thresholdEditText.error = "Please enter a valid percentage (0-100)"
                return@setOnClickListener
            }
            
            currentThreshold = threshold
            loadLowAttendanceStudents(threshold)
        }
        
        exportPdfButton.setOnClickListener {
            exportToPdf()
        }
        
    }
    
    private fun loadLowAttendanceStudents(threshold: Double) {
        showLoading(true)
        
        val api = ApiClient.getClient().create(AttendanceApi::class.java)
        api.getLowAttendanceStudentsSync(threshold).enqueue(object : Callback<List<AttendanceStatistics>> {
            override fun onResponse(call: Call<List<AttendanceStatistics>>, response: Response<List<AttendanceStatistics>>) {
                showLoading(false)
                
                if (response.isSuccessful) {
                    currentStudents = response.body() ?: emptyList()
                    adapter.updateStudents(currentStudents)
                    updateStatus()
                } else {
                    statusTextView.text = "Failed to load data: ${response.code()}"
                    statusTextView.visibility = View.VISIBLE
                    Toast.makeText(this@LowAttendanceReportActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }
            
            override fun onFailure(call: Call<List<AttendanceStatistics>>, t: Throwable) {
                showLoading(false)
                statusTextView.text = "Network error: ${t.message}"
                statusTextView.visibility = View.VISIBLE
                Toast.makeText(this@LowAttendanceReportActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    private fun updateStatus() {
        if (currentStudents.isEmpty()) {
            statusTextView.text = "Great! No students have attendance below $currentThreshold%"
            statusTextView.visibility = View.VISIBLE
            exportPdfButton.isEnabled = false
        } else {
            statusTextView.text = "${currentStudents.size} students have attendance below $currentThreshold%"
            statusTextView.visibility = View.VISIBLE
            exportPdfButton.isEnabled = true
        }
    }
    
    private fun exportToPdf() {
        if (currentStudents.isEmpty()) {
            Toast.makeText(this, "No data to export", Toast.LENGTH_SHORT).show()
            return
        }
        
        showLoading(true)
        
        Thread {
            val filePath = ExportUtils.exportToPdf(this, currentStudents, currentThreshold)
            
            runOnUiThread {
                showLoading(false)
                if (filePath != null) {
                    ExportUtils.showExportSuccess(this, filePath)
                } else {
                    ExportUtils.showExportError(this)
                }
            }
        }.start()
    }
    
    private fun showLoading(isLoading: Boolean) {
        loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        loadButton.isEnabled = !isLoading
        exportPdfButton.isEnabled = !isLoading && currentStudents.isNotEmpty()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}