package com.example.attendanceapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.attendanceapp.R
import com.example.attendanceapp.model.AttendanceRecord
import com.example.attendanceapp.model.AttendanceStatus
import java.text.SimpleDateFormat
import java.util.*

class AttendanceRecordAdapter(
    private val records: List<AttendanceRecord>,
    private val onEditClick: (AttendanceRecord) -> Unit,
    private val onDeleteClick: (AttendanceRecord) -> Unit
) : RecyclerView.Adapter<AttendanceRecordAdapter.ViewHolder>() {

    private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attendance_record, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = records[position]
        holder.bind(record, onEditClick, onDeleteClick)
    }

    override fun getItemCount(): Int = records.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val statusIcon: TextView = itemView.findViewById(R.id.statusIcon)
        private val dateText: TextView = itemView.findViewById(R.id.dateText)
        private val statusText: TextView = itemView.findViewById(R.id.statusText)
        private val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        fun bind(
            record: AttendanceRecord,
            onEditClick: (AttendanceRecord) -> Unit,
            onDeleteClick: (AttendanceRecord) -> Unit
        ) {
            // Format the date
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val displayFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                val date = inputFormat.parse(record.date)
                dateText.text = date?.let { displayFormat.format(it) } ?: record.date
            } catch (e: Exception) {
                dateText.text = record.date
            }
            
            // Set status information
            when (record.status) {
                AttendanceStatus.PRESENT -> {
                    statusIcon.text = "✓"
                    statusIcon.setBackgroundColor(itemView.context.getColor(android.R.color.holo_green_dark))
                    statusText.text = "Present"
                    statusText.setTextColor(itemView.context.getColor(android.R.color.holo_green_dark))
                }
                AttendanceStatus.ABSENT -> {
                    statusIcon.text = "✗"
                    statusIcon.setBackgroundColor(itemView.context.getColor(android.R.color.holo_red_dark))
                    statusText.text = "Absent"
                    statusText.setTextColor(itemView.context.getColor(android.R.color.holo_red_dark))
                }
            }

            // Set button click listeners
            btnEdit.setOnClickListener { onEditClick(record) }
            btnDelete.setOnClickListener { onDeleteClick(record) }
        }
    }
}