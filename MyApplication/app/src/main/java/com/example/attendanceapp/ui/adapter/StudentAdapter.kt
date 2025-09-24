package com.example.attendanceapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.attendanceapp.R
import com.example.attendanceapp.model.Student

class StudentAdapter(private val students: List<Student>) :
    RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    private val attendanceMap = mutableMapOf<Long, Boolean>()

    init {
        students.forEach { student ->
            student.id?.let { attendanceMap[it] = false }
        }
    }

    fun getAttendanceMap(): Map<Long, Boolean> = attendanceMap
    
    fun selectAll() {
        students.forEach { student ->
            student.id?.let { attendanceMap[it] = true }
        }
        notifyDataSetChanged()
    }
    
    fun clearAll() {
        students.forEach { student ->
            student.id?.let { attendanceMap[it] = false }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = students[position]
        val studentId = student.id ?: return
        holder.bind(student, attendanceMap[studentId] ?: false) { isChecked ->
            attendanceMap[studentId] = isChecked
        }
    }

    override fun getItemCount(): Int = students.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.studentName)
        private val studentId: TextView = itemView.findViewById(R.id.studentId)
        private val avatar: TextView = itemView.findViewById(R.id.studentAvatar)
        private val checkbox: CheckBox = itemView.findViewById(R.id.attendanceCheckbox)
        
        fun bind(student: Student, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
            name.text = student.name
            studentId.text = "Roll: ${student.rollNumber}"
            avatar.text = student.name.firstOrNull()?.uppercase() ?: "S"
            
            // Remove listener temporarily to avoid triggering during setup
            checkbox.setOnCheckedChangeListener(null)
            checkbox.isChecked = isChecked
            
            // Set the listener back
            checkbox.setOnCheckedChangeListener { _, checked ->
                onCheckedChange(checked)
            }
        }
    }
}
