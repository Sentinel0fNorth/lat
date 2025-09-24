package com.example.attendanceapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import com.example.attendanceapp.api.ApiClient
import com.example.attendanceapp.api.AttendanceApi
import com.example.attendanceapp.databinding.ActivityMainBinding
import com.example.attendanceapp.model.Student
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setupUI()
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback: show a simple error message and close
            Toast.makeText(this, "Error starting app: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }
    
    private fun setupUI() {
        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            
            if (validateInput(username, password)) {
                // Simple authentication - in real app, you'd call an API
                if (username == "teacher" && password == "password") {
                    navigateToAttendance()
                } else {
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        binding.guestButton.setOnClickListener {
            navigateToAttendance()
        }
        
        // Add a button to create new student (if such button exists in layout)
        // This is a placeholder - you'd need to add this button to your layout
        // binding.addStudentButton?.setOnClickListener {
        //     showAddStudentDialog()
        // }
    }
    
    private fun validateInput(username: String, password: String): Boolean {
        if (username.isEmpty()) {
            binding.usernameEditText.error = "Username is required"
            return false
        }
        
        if (password.isEmpty()) {
            binding.passwordEditText.error = "Password is required"
            return false
        }
        
        return true
    }
    
    private fun navigateToAttendance() {
        val intent = Intent(this, AttendanceActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    private fun showAddStudentDialog() {
        val dialogView = layoutInflater.inflate(android.R.layout.simple_list_item_2, null)
        
        val nameEditText = EditText(this).apply {
            hint = "Student Name"
        }
        val rollEditText = EditText(this).apply {
            hint = "Roll Number"
        }
        
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add New Student")
            .setView(android.widget.LinearLayout(this).apply {
                orientation = android.widget.LinearLayout.VERTICAL
                addView(nameEditText)
                addView(rollEditText)
            })
            .setPositiveButton("Add") { _, _ ->
                val name = nameEditText.text.toString().trim()
                val rollNumber = rollEditText.text.toString().trim()
                
                if (name.isNotEmpty() && rollNumber.isNotEmpty()) {
                    createStudent(name, rollNumber)
                } else {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        
        dialog.show()
    }
    
    private fun createStudent(name: String, rollNumber: String) {
        val student = Student(null, name, rollNumber)
        val api = ApiClient.getClient().create(AttendanceApi::class.java)
        
        api.createStudentSync(student).enqueue(object : Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity, 
                        "Student added successfully!", 
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@MainActivity, 
                        "Failed to add student: ${response.code()}", 
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            
            override fun onFailure(call: Call<Student>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity, 
                    "Network error: ${t.message}", 
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}