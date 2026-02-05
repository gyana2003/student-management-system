package com.student.backend.controller;

import com.student.backend.model.Marks;
import com.student.backend.service.MarksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/marks")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class MarksController {

    @Autowired
    private MarksService marksService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Marks>> getStudentMarks(@PathVariable Long studentId) {
        return ResponseEntity.ok(marksService.getStudentMarks(studentId));
    }

    @GetMapping("/student/{studentId}/average")
    public ResponseEntity<Double> getAverageMarks(@PathVariable Long studentId) {
        return ResponseEntity.ok(marksService.getAverageMarks(studentId));
    }

    @GetMapping("/student/{studentId}/subject/{subject}")
    public ResponseEntity<List<Marks>> getMarksBySubject(
            @PathVariable Long studentId,
            @PathVariable String subject) {
        return ResponseEntity.ok(marksService.getMarksBySubject(studentId, subject));
    }

    @GetMapping("/student/{studentId}/exam/{examType}")
    public ResponseEntity<List<Marks>> getMarksByExamType(
            @PathVariable Long studentId,
            @PathVariable String examType) {
        return ResponseEntity.ok(marksService.getMarksByExamType(studentId, examType));
    }

    @PostMapping("/student/{studentId}")
    public ResponseEntity<Marks> addMarks(
            @PathVariable Long studentId,
            @RequestBody Marks marks) {
        return ResponseEntity.ok(marksService.addMarks(studentId, marks));
    }

    @PutMapping("/{marksId}")
    public ResponseEntity<Marks> updateMarks(
            @PathVariable Long marksId,
            @RequestBody Marks marks) {
        return ResponseEntity.ok(marksService.updateMarks(marksId, marks));
    }

    @DeleteMapping("/{marksId}")
    public ResponseEntity<Void> deleteMarks(@PathVariable Long marksId) {
        marksService.deleteMarks(marksId);
        return ResponseEntity.ok().build();
    }
}
