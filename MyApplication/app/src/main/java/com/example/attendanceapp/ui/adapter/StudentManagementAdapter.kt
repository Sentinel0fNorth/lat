package com.example.attendanceapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.attendanceapp.R
import com.example.attendanceapp.model.AttendanceStatistics
import com.example.attendanceapp.model.Student

class StudentManagementAdapter(
    private val students: List<Student>,
    private val statistics: List<AttendanceStatistics>,
    private val onEditClick: (Student) -> Unit,
    private val onDeleteClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentManagementAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student_management, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = students[position]
        val stats = statistics.find { it.studentId == student.id }
        holder.bind(student, stats, onEditClick, onDeleteClick)
    }

    override fun getItemCount(): Int = students.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatar: TextView = itemView.findViewById(R.id.studentAvatar)
        private val name: TextView = itemView.findViewById(R.id.studentName)
        private val roll: TextView = itemView.findViewById(R.id.studentRoll)
        private val attendanceInfo: TextView = itemView.findViewById(R.id.attendanceInfo)
        private val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        fun bind(
            student: Student,
            stats: AttendanceStatistics?,
            onEditClick: (Student) -> Unit,
            onDeleteClick: (Student) -> Unit
        ) {
            // Display full student name prominently
            name.text = student.name
            roll.text = "Roll Number: ${student.rollNumber}"
            
            // Set avatar to first letter of student's name
            val firstLetter = student.name.trim().firstOrNull()?.uppercase() ?: "?"
            avatar.text = firstLetter
            
            // Display attendance statistics with better formatting
            if (stats != null) {
                val percentage = String.format("%.1f", stats.attendancePercentage)
                attendanceInfo.text = "Attendance: $percentage% (${stats.presentDays}/${stats.totalDays} days)"
                
                // Color-code attendance percentage
                val context = itemView.context
                val color = when {
                    stats.attendancePercentage >= 75.0 -> context.getColor(android.R.color.holo_green_dark)
                    stats.attendancePercentage >= 50.0 -> context.getColor(android.R.color.holo_orange_dark)
                    else -> context.getColor(android.R.color.holo_red_dark)
                }
                attendanceInfo.setTextColor(color)
            } else {
                attendanceInfo.text = "Attendance: No data available"
                attendanceInfo.setTextColor(itemView.context.getColor(android.R.color.darker_gray))
            }

            // Set button click listeners
            btnEdit.setOnClickListener { onEditClick(student) }
            btnDelete.setOnClickListener { onDeleteClick(student) }
        }
    }
}