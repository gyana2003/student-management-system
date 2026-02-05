package com.student.backend.service;

import com.student.backend.model.Attendance;
import com.student.backend.model.Student;
import com.student.backend.repository.AttendanceRepository;
import com.student.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    public Attendance addAttendance(Long studentId, Attendance attendance) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        attendance.setStudent(student);
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getStudentAttendance(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return attendanceRepository.findByStudent(student);
    }

    public List<Attendance> getAttendanceByDateRange(Long studentId, LocalDate startDate, LocalDate endDate) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return attendanceRepository.findByStudentAndAttendanceDateBetween(student, startDate, endDate);
    }

    public double getAttendancePercentage(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        List<Attendance> attendances = attendanceRepository.findByStudent(student);
        if (attendances.isEmpty()) return 0;

        long presentCount = attendances.stream()
                .filter(a -> a.getStatus() == Attendance.AttendanceStatus.PRESENT)
                .count();
        return (presentCount * 100.0) / attendances.size();
    }

    public Attendance updateAttendance(Long attendanceId, Attendance updatedAttendance) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));
        attendance.setStatus(updatedAttendance.getStatus());
        attendance.setRemarks(updatedAttendance.getRemarks());
        return attendanceRepository.save(attendance);
    }

    public void deleteAttendance(Long attendanceId) {
        attendanceRepository.deleteById(attendanceId);
    }
}
