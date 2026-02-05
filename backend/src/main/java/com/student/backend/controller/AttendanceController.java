package com.student.backend.controller;

import com.student.backend.model.Attendance;
import com.student.backend.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Attendance>> getStudentAttendance(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getStudentAttendance(studentId));
    }

    @GetMapping("/student/{studentId}/percentage")
    public ResponseEntity<Double> getAttendancePercentage(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getAttendancePercentage(studentId));
    }

    @GetMapping("/student/{studentId}/range")
    public ResponseEntity<List<Attendance>> getAttendanceByDateRange(
            @PathVariable Long studentId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(attendanceService.getAttendanceByDateRange(studentId, startDate, endDate));
    }

    @PostMapping("/student/{studentId}")
    public ResponseEntity<Attendance> addAttendance(
            @PathVariable Long studentId,
            @RequestBody Attendance attendance) {
        return ResponseEntity.ok(attendanceService.addAttendance(studentId, attendance));
    }

    @PutMapping("/{attendanceId}")
    public ResponseEntity<Attendance> updateAttendance(
            @PathVariable Long attendanceId,
            @RequestBody Attendance attendance) {
        return ResponseEntity.ok(attendanceService.updateAttendance(attendanceId, attendance));
    }

    @DeleteMapping("/{attendanceId}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long attendanceId) {
        attendanceService.deleteAttendance(attendanceId);
        return ResponseEntity.ok().build();
    }
}
