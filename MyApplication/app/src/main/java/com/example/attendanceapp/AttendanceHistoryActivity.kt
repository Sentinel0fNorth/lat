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
        
        // For now, show a message that this feature is coming soon
        // since the backend model doesn't match the frontend expectations perfectly
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            showLoading(false)
            Toast.makeText(
                this@AttendanceHistoryActivity, 
                "Attendance history feature coming soon!\nCurrently, attendance is being submitted successfully.", 
                Toast.LENGTH_LONG
            ).show()
            showEmptyState()
        }, 1000)
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