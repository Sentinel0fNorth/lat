package com.example.attendanceapp

import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.attendanceapp.model.Student

object StudentDialogHelper {

    fun createAddStudentDialog(
        context: Context,
        onSave: (name: String, rollNumber: String) -> Unit
    ): AlertDialog {
        val view = android.view.LayoutInflater.from(context).inflate(R.layout.dialog_add_edit_student, null)
        
        val dialogTitle = view.findViewById<TextView>(R.id.dialogTitle)
        val etStudentName = view.findViewById<EditText>(R.id.etStudentName)
        val etRollNumber = view.findViewById<EditText>(R.id.etRollNumber)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        
        dialogTitle.text = "Add Student"

        val dialog = AlertDialog.Builder(context)
            .setView(view)
            .create()

        btnSave.setOnClickListener {
            val name = etStudentName.text.toString().trim()
            val rollNumber = etRollNumber.text.toString().trim()

            if (validateInput(etStudentName, etRollNumber, name, rollNumber)) {
                onSave(name, rollNumber)
                dialog.dismiss()
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        return dialog
    }

    fun createEditStudentDialog(
        context: Context,
        student: Student,
        onSave: (name: String, rollNumber: String) -> Unit
    ): AlertDialog {
        val view = android.view.LayoutInflater.from(context).inflate(R.layout.dialog_add_edit_student, null)
        
        val dialogTitle = view.findViewById<TextView>(R.id.dialogTitle)
        val etStudentName = view.findViewById<EditText>(R.id.etStudentName)
        val etRollNumber = view.findViewById<EditText>(R.id.etRollNumber)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        
        dialogTitle.text = "Edit Student"
        etStudentName.setText(student.name)
        etRollNumber.setText(student.rollNumber)

        val dialog = AlertDialog.Builder(context)
            .setView(view)
            .create()

        btnSave.setOnClickListener {
            val name = etStudentName.text.toString().trim()
            val rollNumber = etRollNumber.text.toString().trim()

            if (validateInput(etStudentName, etRollNumber, name, rollNumber)) {
                onSave(name, rollNumber)
                dialog.dismiss()
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        return dialog
    }

    private fun validateInput(
        etStudentName: EditText,
        etRollNumber: EditText,
        name: String,
        rollNumber: String
    ): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            etStudentName.error = "Name is required"
            isValid = false
        } else {
            etStudentName.error = null
        }

        if (rollNumber.isEmpty()) {
            etRollNumber.error = "Roll number is required"
            isValid = false
        } else {
            etRollNumber.error = null
        }

        return isValid
    }
}
