package com.student.backend.controller;

import com.student.backend.dto.AnalyticsDTO;
import com.student.backend.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<AnalyticsDTO> getStudentAnalytics(@PathVariable Long studentId) {
        System.out.println("Fetching analytics for student: " + studentId);
        return ResponseEntity.ok(analyticsService.getStudentAnalytics(studentId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AnalyticsDTO>> getAllStudentsAnalytics() {
        System.out.println("Fetching analytics for all students");
        return ResponseEntity.ok(analyticsService.getAllStudentsAnalytics());
    }
}
