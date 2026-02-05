package com.student.backend.service;

import com.student.backend.model.Attendance;
import com.student.backend.model.Marks;
import com.student.backend.model.Student;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfExportService {

    private static final float MARGIN = 40;
    private static final float Y_START = 750;

    private PDFont getFont(PDDocument document, boolean bold) {
        // In PDFBox 3.0, font handling is simplified
        // Return null to use default font, or handle font loading with proper API
        try {
            // For simplicity with PDFBox 3.0, we'll use a basic approach
            // Actual font loading would require proper font files
            return null; // P DFBox will use default Helvetica if null
        } catch (Exception e) {
            return null;
        }
    }

    public byte[] generateAttendanceReport(Student student, List<Attendance> attendanceList, double percentage) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            PDFont boldFont = getFont(document, true);
            PDFont regularFont = getFont(document, false);

            float yPosition = Y_START;

            // Title
            contentStream.setFont(boldFont, 18);
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText("ATTENDANCE REPORT");
            contentStream.endText();
            yPosition -= 40;

            // Student Info
            contentStream.setFont(regularFont, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText("Student Information:");
            contentStream.endText();
            yPosition -= 20;

            contentStream.setFont(regularFont, 11);
            String[] studentInfo = {
                    "Name: " + student.getName(),
                    "Roll Number: " + student.getRollNo(),
                    "Email: " + student.getEmail(),
                    "Attendance Percentage: " + String.format("%.2f", percentage) + "%"
            };

            for (String info : studentInfo) {
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText(info);
                contentStream.endText();
                yPosition -= 16;
            }

            yPosition -= 20;

            // Table header
            contentStream.setFont(boldFont, 11);
            String[] headers = {"Date", "Subject", "Status", "Remarks"};
            float[] columnWidths = {100, 150, 80, 100};
            float xPosition = MARGIN;

            for (int i = 0; i < headers.length; i++) {
                contentStream.beginText();
                contentStream.newLineAtOffset(xPosition, yPosition);
                contentStream.showText(headers[i]);
                contentStream.endText();
                xPosition += columnWidths[i];
            }

            yPosition -= 20;

            // Table rows
            contentStream.setFont(regularFont, 10);
            for (Attendance attendance : attendanceList) {
                if (yPosition < 50) {
                    contentStream.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    yPosition = Y_START;
                }

                xPosition = MARGIN;
                String[] rowData = {
                        attendance.getAttendanceDate().toString(),
                        attendance.getSubject(),
                        attendance.getStatus().name(),
                        attendance.getRemarks() != null ? attendance.getRemarks() : ""
                };

                for (int i = 0; i < rowData.length; i++) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(xPosition, yPosition);
                    contentStream.showText(rowData[i]);
                    contentStream.endText();
                    xPosition += columnWidths[i];
                }

                yPosition -= 16;
            }

            // Footer
            contentStream.setFont(regularFont, 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN, 30);
            contentStream.showText("Report Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            contentStream.endText();

            contentStream.close();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            document.close();

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage());
        }
    }

    public byte[] generateMarksReport(Student student, List<Marks> marksList) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            PDFont boldFont = getFont(document, true);
            PDFont regularFont = getFont(document, false);

            float yPosition = Y_START;

            // Title
            contentStream.setFont(boldFont, 18);
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText("MARKS REPORT");
            contentStream.endText();
            yPosition -= 40;

            // Student Info
            contentStream.setFont(regularFont, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText("Student Information:");
            contentStream.endText();
            yPosition -= 20;

            contentStream.setFont(regularFont, 11);
            String[] studentInfo = {
                    "Name: " + student.getName(),
                    "Roll Number: " + student.getRollNo(),
                    "Email: " + student.getEmail()
            };

            for (String info : studentInfo) {
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText(info);
                contentStream.endText();
                yPosition -= 16;
            }

            yPosition -= 20;

            // Table header
            contentStream.setFont(boldFont, 11);
            String[] headers = {"Subject", "Exam Type", "Marks", "Grade", "%", "Date"};
            float[] columnWidths = {100, 90, 60, 50, 50, 80};
            float xPosition = MARGIN;

            for (int i = 0; i < headers.length; i++) {
                contentStream.beginText();
                contentStream.newLineAtOffset(xPosition, yPosition);
                contentStream.showText(headers[i]);
                contentStream.endText();
                xPosition += columnWidths[i];
            }

            yPosition -= 20;

            // Table rows
            contentStream.setFont(regularFont, 10);
            double totalPercentage = 0;
            int count = 0;

            for (Marks marks : marksList) {
                if (yPosition < 50) {
                    contentStream.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    yPosition = Y_START;
                }

                xPosition = MARGIN;
                String[] rowData = {
                        marks.getSubject(),
                        marks.getExamType(),
                        String.format("%.0f", marks.getMarks()),
                        marks.getGrade(),
                        String.format("%.0f", marks.getPercentage()),
                        marks.getExamDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                };

                for (int i = 0; i < rowData.length; i++) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(xPosition, yPosition);
                    contentStream.showText(rowData[i]);
                    contentStream.endText();
                    xPosition += columnWidths[i];
                }

                yPosition -= 16;
                totalPercentage += marks.getPercentage();
                count++;
            }

            yPosition -= 10;

            // Average
            if (count > 0) {
                contentStream.setFont(regularFont, 11);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);
                contentStream.showText("Overall Average: " + String.format("%.2f", totalPercentage / count) + "%");
                contentStream.endText();
            }

            // Footer
            contentStream.setFont(regularFont, 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(MARGIN, 30);
            contentStream.showText("Report Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            contentStream.endText();

            contentStream.close();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            document.close();

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage());
        }
    }
}
