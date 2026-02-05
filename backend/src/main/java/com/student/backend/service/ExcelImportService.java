package com.student.backend.service;

import com.student.backend.model.Attendance;
import com.student.backend.model.Attendance.AttendanceStatus;
import com.student.backend.model.Marks;
import com.student.backend.model.Student;
import com.student.backend.repository.AttendanceRepository;
import com.student.backend.repository.MarksRepository;
import com.student.backend.repository.StudentRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ExcelImportService {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final MarksRepository marksRepository;

    public ExcelImportService(StudentRepository studentRepository, AttendanceRepository attendanceRepository,
                              MarksRepository marksRepository) {
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
        this.marksRepository = marksRepository;
    }

    public Map<String, Object> importAttendanceFromExcel(MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        List<String> messages = new ArrayList<>();
        int successCount = 0;
        int errorCount = 0;

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = 0;

            for (Row row : sheet) {
                rowCount++;
                if (rowCount == 1) continue; // Skip header row

                try {
                    String rollNumber = getCellValueAsString(row.getCell(0));
                    String subject = getCellValueAsString(row.getCell(1));
                    String status = getCellValueAsString(row.getCell(2));
                    String remarks = getCellValueAsString(row.getCell(3));
                    LocalDate date = getCellValueAsDate(row.getCell(4));

                    if (rollNumber == null || rollNumber.isEmpty()) {
                        messages.add("Row " + rowCount + ": Roll number is required");
                        errorCount++;
                        continue;
                    }

                    Optional<Student> student = studentRepository.findByRollNo(rollNumber);
                    if (student.isEmpty()) {
                        messages.add("Row " + rowCount + ": Student with roll number " + rollNumber + " not found");
                        errorCount++;
                        continue;
                    }

                    Attendance attendance = Attendance.builder()
                            .student(student.get())
                            .subject(subject != null ? subject : "Unknown")
                            .status(AttendanceStatus.valueOf(status.toUpperCase()))
                            .remarks(remarks)
                            .attendanceDate(date != null ? date : LocalDate.now())
                            .build();

                    attendanceRepository.save(attendance);
                    successCount++;
                } catch (IllegalArgumentException e) {
                    messages.add("Row " + rowCount + ": Invalid status value - " + e.getMessage());
                    errorCount++;
                } catch (Exception e) {
                    messages.add("Row " + rowCount + ": Error - " + e.getMessage());
                    errorCount++;
                }
            }
        } catch (IOException e) {
            response.put("error", "Failed to read Excel file: " + e.getMessage());
            return response;
        }

        response.put("success", successCount);
        response.put("errors", errorCount);
        response.put("messages", messages);
        return response;
    }

    public Map<String, Object> importMarksFromExcel(MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        List<String> messages = new ArrayList<>();
        int successCount = 0;
        int errorCount = 0;

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = 0;

            for (Row row : sheet) {
                rowCount++;
                if (rowCount == 1) continue; // Skip header row

                try {
                    String rollNumber = getCellValueAsString(row.getCell(0));
                    String subject = getCellValueAsString(row.getCell(1));
                    String examType = getCellValueAsString(row.getCell(2));
                    double marks = getCellValueAsDouble(row.getCell(3));
                    LocalDate examDate = getCellValueAsDate(row.getCell(4));

                    if (rollNumber == null || rollNumber.isEmpty()) {
                        messages.add("Row " + rowCount + ": Roll number is required");
                        errorCount++;
                        continue;
                    }

                    Optional<Student> student = studentRepository.findByRollNo(rollNumber);
                    if (student.isEmpty()) {
                        messages.add("Row " + rowCount + ": Student with roll number " + rollNumber + " not found");
                        errorCount++;
                        continue;
                    }

                    LocalDateTime examDateTime = examDate != null ? examDate.atStartOfDay() : LocalDate.now().atStartOfDay();
                    Marks marksRecord = Marks.builder()
                            .student(student.get())
                            .subject(subject != null ? subject : "Unknown")
                            .examType(examType != null ? examType : "Unit Test")
                            .marks(marks)
                            .totalMarks(100.0)
                            .examDate(examDateTime)
                            .build();
                    // Grade will be auto-calculated in @PrePersist

                    marksRepository.save(marksRecord);
                    successCount++;
                } catch (Exception e) {
                    messages.add("Row " + rowCount + ": Error - " + e.getMessage());
                    errorCount++;
                }
            }
        } catch (IOException e) {
            response.put("error", "Failed to read Excel file: " + e.getMessage());
            return response;
        }

        response.put("success", successCount);
        response.put("errors", errorCount);
        response.put("messages", messages);
        return response;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            default:
                return null;
        }
    }

    private double getCellValueAsDouble(Cell cell) {
        if (cell == null) return 0.0;
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        }
        return 0.0;
    }

    private LocalDate getCellValueAsDate(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getDateCellValue().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }
        return null;
    }

    public byte[] generateAttendanceTemplate() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Attendance");
            
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Roll Number", "Subject", "Status", "Remarks", "Attendance Date"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(getHeaderStyle(workbook));
            }

            // Add sample data row
            Row sampleRow = sheet.createRow(1);
            sampleRow.createCell(0).setCellValue("STU001");
            sampleRow.createCell(1).setCellValue("Mathematics");
            sampleRow.createCell(2).setCellValue("PRESENT");
            sampleRow.createCell(3).setCellValue("Regular");
            sampleRow.createCell(4).setCellValue("2026-02-05");

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate template: " + e.getMessage());
        }
    }

    public byte[] generateMarksTemplate() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Marks");
            
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Roll Number", "Subject", "Exam Type", "Marks", "Exam Date"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(getHeaderStyle(workbook));
            }

            // Add sample data row
            Row sampleRow = sheet.createRow(1);
            sampleRow.createCell(0).setCellValue("STU001");
            sampleRow.createCell(1).setCellValue("Mathematics");
            sampleRow.createCell(2).setCellValue("Unit Test");
            sampleRow.createCell(3).setCellValue(85.5);
            sampleRow.createCell(4).setCellValue("2026-02-05");

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate template: " + e.getMessage());
        }
    }

    private CellStyle getHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
}
