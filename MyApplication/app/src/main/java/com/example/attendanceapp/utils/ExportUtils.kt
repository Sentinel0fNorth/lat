package com.example.attendanceapp.utils

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.example.attendanceapp.model.AttendanceStatistics
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.UnitValue
import com.itextpdf.layout.element.Cell
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.io.font.constants.StandardFonts
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object ExportUtils {
    
    private fun getExportDirectory(context: Context): File? {
        return try {
            val exportDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "AttendanceReports")
            if (!exportDir.exists()) {
                exportDir.mkdirs()
            }
            exportDir
        } catch (e: Exception) {
            null
        }
    }
    
    fun exportToPdf(context: Context, students: List<AttendanceStatistics>, threshold: Double): String? {
        return try {
            val exportDir = getExportDirectory(context) ?: return null
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "low_attendance_report_$timestamp.pdf"
            val file = File(exportDir, fileName)
            
            val writer = PdfWriter(file)
            val pdfDoc = PdfDocument(writer)
            val document = Document(pdfDoc)
            
            // Add title
            val titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)
            val title = Paragraph("Low Attendance Report")
                .setFont(titleFont)
                .setFontSize(18f)
                .setMarginBottom(20f)
            document.add(title)
            
            // Add report details
            val reportDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
            document.add(Paragraph("Generated on: $reportDate").setMarginBottom(10f))
            document.add(Paragraph("Threshold: ${threshold}%").setMarginBottom(20f))
            
            if (students.isEmpty()) {
                document.add(Paragraph("No students found with attendance below ${threshold}%"))
            } else {
                // Create table
                val table = Table(UnitValue.createPercentArray(floatArrayOf(10f, 40f, 20f, 15f, 15f)))
                    .setWidth(UnitValue.createPercentValue(100f))
                
                // Add headers
                val headerFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)
                table.addHeaderCell(Cell().add(Paragraph("S.No").setFont(headerFont)))
                table.addHeaderCell(Cell().add(Paragraph("Student Name").setFont(headerFont)))
                table.addHeaderCell(Cell().add(Paragraph("Roll Number").setFont(headerFont)))
                table.addHeaderCell(Cell().add(Paragraph("Present").setFont(headerFont)))
                table.addHeaderCell(Cell().add(Paragraph("Attendance %").setFont(headerFont)))
                
                // Add data rows
                students.forEachIndexed { index, student ->
                    table.addCell(Cell().add(Paragraph((index + 1).toString())))
                    table.addCell(Cell().add(Paragraph(student.studentName)))
                    table.addCell(Cell().add(Paragraph(student.rollNumber ?: "N/A")))
                    table.addCell(Cell().add(Paragraph("${student.presentDays}/${student.totalDays}")))
                    table.addCell(Cell().add(Paragraph(String.format("%.1f%%", student.attendancePercentage))))
                }
                
                document.add(table)
                
                // Add summary
                document.add(Paragraph("\nSummary:").setFont(titleFont).setMarginTop(20f))
                document.add(Paragraph("Total students with low attendance: ${students.size}"))
                val avgAttendance = students.map { it.attendancePercentage }.average()
                document.add(Paragraph("Average attendance of listed students: ${String.format("%.1f%%", avgAttendance)}"))
            }
            
            document.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    fun showExportSuccess(context: Context, filePath: String) {
        val fileName = File(filePath).name
        Toast.makeText(
            context,
            "PDF exported successfully!\nFile: $fileName\nLocation: ${File(filePath).parent}",
            Toast.LENGTH_LONG
        ).show()
    }
    
    fun showExportError(context: Context) {
        Toast.makeText(
            context,
            "Failed to export PDF. Please check storage permissions.",
            Toast.LENGTH_LONG
        ).show()
    }
}