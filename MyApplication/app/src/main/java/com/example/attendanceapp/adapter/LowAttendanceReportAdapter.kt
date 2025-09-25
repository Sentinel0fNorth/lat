package com.example.attendanceapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.attendanceapp.R
import com.example.attendanceapp.model.AttendanceStatistics

class LowAttendanceReportAdapter(private var students: List<AttendanceStatistics>) : RecyclerView.Adapter<LowAttendanceReportAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val rollNumberTextView: TextView = view.findViewById(R.id.rollNumberTextView)
        val attendanceTextView: TextView = view.findViewById(R.id.attendanceTextView)
        val percentageTextView: TextView = view.findViewById(R.id.percentageTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_low_attendance_report, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = students[position]
        
        holder.nameTextView.text = student.studentName
        holder.rollNumberTextView.text = student.rollNumber ?: "N/A"
        holder.attendanceTextView.text = "${student.presentDays}/${student.totalDays}"
        holder.percentageTextView.text = String.format("%.1f%%", student.attendancePercentage)
        
        // Color code the percentage based on how low it is
        val percentage = student.attendancePercentage
        when {
            percentage < 50 -> holder.percentageTextView.setTextColor(
                holder.itemView.context.getColor(android.R.color.holo_red_dark)
            )
            percentage < 65 -> holder.percentageTextView.setTextColor(
                holder.itemView.context.getColor(android.R.color.holo_orange_dark)
            )
            else -> holder.percentageTextView.setTextColor(
                holder.itemView.context.getColor(android.R.color.holo_blue_dark)
            )
        }
    }

    override fun getItemCount(): Int = students.size

    fun updateStudents(newStudents: List<AttendanceStatistics>) {
        this.students = newStudents
        notifyDataSetChanged()
    }
}