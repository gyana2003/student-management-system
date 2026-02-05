package com.student.backend.controller;

import com.student.backend.model.Student;
import com.student.backend.repository.StudentRepository;
import com.student.backend.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class EmailController {

    private final EmailService emailService;
    private final StudentRepository studentRepository;

    public EmailController(EmailService emailService, StudentRepository studentRepository) {
        this.emailService = emailService;
        this.studentRepository = studentRepository;
    }

    @PostMapping("/send-attendance-notification/{studentId}")
    public ResponseEntity<?> sendAttendanceNotification(
            @PathVariable Long studentId,
            @RequestParam double percentage,
            @RequestParam String status) {
        
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String fullName = student.get().getName();
        emailService.sendAttendanceNotification(student.get().getEmail(), fullName, percentage, status);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Attendance notification sent successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-marks-notification/{studentId}")
    public ResponseEntity<?> sendMarksNotification(
            @PathVariable Long studentId,
            @RequestParam double marks,
            @RequestParam String grade,
            @RequestParam String subject) {
        
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String fullName = student.get().getName();
        emailService.sendMarksNotification(student.get().getEmail(), fullName, marks, grade, subject);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Marks notification sent successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-bulk-notification")
    public ResponseEntity<?> sendBulkNotification(
            @RequestParam String subject,
            @RequestParam String body) {
        
        List<Student> students = studentRepository.findAll();
        int successCount = 0;

        for (Student student : students) {
            try {
                emailService.sendBulkNotification(student.getEmail(), subject, body);
                successCount++;
            } catch (Exception e) {
                System.err.println("Failed to send email to " + student.getEmail() + ": " + e.getMessage());
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bulk notification sent");
        response.put("successCount", successCount);
        response.put("totalStudents", students.size());
        return ResponseEntity.ok(response);
    }
}
