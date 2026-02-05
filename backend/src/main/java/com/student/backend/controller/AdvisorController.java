package com.student.backend.controller;

import com.student.backend.model.AdvisorSession;
import com.student.backend.service.AdvisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/advisor")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class AdvisorController {

    @Autowired
    private AdvisorService advisorService;

    @PostMapping("/student/{studentId}/session")
    public ResponseEntity<AdvisorSession> createSession(
            @PathVariable Long studentId,
            @RequestBody AdvisorSession session) {
        System.out.println("Creating advisor session for student: " + studentId);
        return ResponseEntity.ok(advisorService.createAdvisorSession(studentId, session));
    }

    @GetMapping("/student/{studentId}/sessions")
    public ResponseEntity<List<AdvisorSession>> getStudentSessions(@PathVariable Long studentId) {
        return ResponseEntity.ok(advisorService.getStudentSessions(studentId));
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<AdvisorSession> getSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(advisorService.getSession(sessionId));
    }

    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long sessionId) {
        advisorService.deleteSession(sessionId);
        return ResponseEntity.ok().build();
    }
}
