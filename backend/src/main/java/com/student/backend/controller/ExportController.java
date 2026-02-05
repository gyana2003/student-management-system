package com.student.backend.controller;

import com.student.backend.model.Attendance;
import com.student.backend.model.Marks;
import com.student.backend.model.Student;
import com.student.backend.repository.AttendanceRepository;
import com.student.backend.repository.MarksRepository;
import com.student.backend.repository.StudentRepository;
import com.student.backend.service.PdfExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/export")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class ExportController {

    private final PdfExportService pdfExportService;
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final MarksRepository marksRepository;

    public ExportController(PdfExportService pdfExportService, StudentRepository studentRepository,
                            AttendanceRepository attendanceRepository, MarksRepository marksRepository) {
        this.pdfExportService = pdfExportService;
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
        this.marksRepository = marksRepository;
    }

    @GetMapping("/attendance/{studentId}")
    public ResponseEntity<byte[]> exportAttendancePDF(@PathVariable Long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Attendance> attendanceList = attendanceRepository.findByStudent(student.get());
        double percentage = attendanceList.isEmpty() ? 0.0 : 
            (attendanceList.stream()
                .filter(a -> a.getStatus().name().equals("PRESENT"))
                .count() * 100.0) / attendanceList.size();

        byte[] pdfContent = pdfExportService.generateAttendanceReport(student.get(), attendanceList, percentage);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"attendance_" + student.get().getRollNo() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfContent);
    }

    @GetMapping("/marks/{studentId}")
    public ResponseEntity<byte[]> exportMarksPDF(@PathVariable Long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Marks> marksList = marksRepository.findByStudent(student.get());
        byte[] pdfContent = pdfExportService.generateMarksReport(student.get(), marksList);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"marks_" + student.get().getRollNo() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfContent);
    }
}
