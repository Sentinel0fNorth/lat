package com.example.attendanceapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.attendanceapp.R
import com.example.attendanceapp.model.AttendanceRecord
import com.example.attendanceapp.model.AttendanceStatus
import java.text.SimpleDateFormat
import java.util.*

class AttendanceHistoryAdapter(private val records: List<AttendanceRecord>) :
    RecyclerView.Adapter<AttendanceHistoryAdapter.ViewHolder>() {

    private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attendance_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(records[position])
    }

    override fun getItemCount(): Int = records.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val studentCountTextView: TextView = itemView.findViewById(R.id.studentCountTextView)
        private val presentCountTextView: TextView = itemView.findViewById(R.id.presentCountTextView)
        private val studentListTextView: TextView = itemView.findViewById(R.id.studentListTextView)
        
        fun bind(record: AttendanceRecord) {
            // Format the date
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val displayFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                val date = inputFormat.parse(record.date)
                dateTextView.text = date?.let { displayFormat.format(it) } ?: record.date
            } catch (e: Exception) {
                dateTextView.text = record.date
            }
            
            // Show student info and status
            val statusText = when (record.status) {
                AttendanceStatus.PRESENT -> "Present: 1"
                AttendanceStatus.ABSENT -> "Absent: 1"
            }
            
            studentCountTextView.text = statusText
            presentCountTextView.text = "${record.student.name} - ${record.status.name.lowercase().replaceFirstChar { it.uppercase() }}"
            studentListTextView.text = "Roll: ${record.student.rollNumber}"
        }
    }
}