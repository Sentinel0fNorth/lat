package com.example.attendanceapp

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendanceapp.api.ApiClient
import com.example.attendanceapp.api.AttendanceApi
import com.example.attendanceapp.databinding.ActivityAttendanceHistoryBinding
import com.example.attendanceapp.model.AttendanceRecord
import com.example.attendanceapp.ui.adapter.AttendanceHistoryAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AttendanceHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAttendanceHistoryBinding
    private lateinit var adapter: AttendanceHistoryAdapter
    // No token needed since we disabled authentication
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        loadAttendanceHistory()
    }

    private fun setupUI() {
        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Attendance History"

        // Setup RecyclerView
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)

        // Setup refresh button
        binding.refreshButton.setOnClickListener {
            loadAttendanceHistory()
        }
    }

    private fun loadAttendanceHistory() {
        showLoading(true)
        
        // Load attendance statistics from backend
        val api = ApiClient.getClient().create(AttendanceApi::class.java)
        api.getAttendanceStatisticsSync().enqueue(object : Callback<List<com.example.attendanceapp.model.AttendanceStatistics>> {
            override fun onResponse(call: Call<List<com.example.attendanceapp.model.AttendanceStatistics>>, response: Response<List<com.example.attendanceapp.model.AttendanceStatistics>>) {
                showLoading(false)
                
                if (response.isSuccessful) {
                    val statistics = response.body() ?: emptyList()
                    if (statistics.isEmpty()) {
                        showEmptyState()
                    } else {
                        showStatisticsData(statistics)
                    }
                } else {
                    showError("Failed to load attendance statistics: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<com.example.attendanceapp.model.AttendanceStatistics>>, t: Throwable) {
                showLoading(false)
                showError("Network error: ${t.message}")
            }
        })
    }
    
    private fun showStatisticsData(statistics: List<com.example.attendanceapp.model.AttendanceStatistics>) {
        // Create a simple display of attendance statistics
        val message = "Attendance Statistics:\n\n" + 
            statistics.joinToString("\n\n") { stat ->
                "${stat.studentName} (${stat.rollNumber})\n" +
                "Present: ${stat.presentDays}/${stat.totalDays} days\n" +
                "Percentage: ${String.format("%.1f", stat.attendancePercentage)}%"
            }
        
        android.app.AlertDialog.Builder(this)
            .setTitle("Attendance History")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                showEmptyState() // Show empty state after closing dialog
            }
            .setCancelable(false)
            .show()
    }
    
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        showEmptyState()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.refreshButton.isEnabled = !isLoading
    }

    private fun showEmptyState() {
        binding.emptyStateLayout.visibility = View.VISIBLE
        binding.historyRecyclerView.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}