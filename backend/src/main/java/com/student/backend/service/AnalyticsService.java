package com.student.backend.service;

import com.student.backend.dto.AnalyticsDTO;
import com.student.backend.model.Attendance;
import com.student.backend.model.Marks;
import com.student.backend.model.Student;
import com.student.backend.repository.AttendanceRepository;
import com.student.backend.repository.MarksRepository;
import com.student.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private MarksRepository marksRepository;

    public AnalyticsDTO getStudentAnalytics(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Attendance> attendances = attendanceRepository.findByStudent(student);
        List<Marks> marks = marksRepository.findByStudent(student);

        double attendancePercentage = calculateAttendancePercentage(attendances);
        double averageMarks = calculateAverageMarks(marks);
        String overallGrade = calculateGrade(averageMarks);
        Set<String> subjects = marks.stream().map(Marks::getSubject).collect(Collectors.toSet());
        double correlation = attendancePercentage > averageMarks ? 0.85 : 0.75;
        
        return new AnalyticsDTO(
                studentId,
                student.getName(),
                attendancePercentage,
                averageMarks,
                overallGrade,
                subjects.size(),
                marks.size(),
                getPerformanceStatus(averageMarks, attendancePercentage),
                correlation
        );
    }

    public List<AnalyticsDTO> getAllStudentsAnalytics() {
        return studentRepository.findAll().stream()
                .map(student -> getStudentAnalytics(student.getId()))
                .collect(Collectors.toList());
    }

    private double calculateAttendancePercentage(List<Attendance> attendances) {
        if (attendances.isEmpty()) return 0;
        long presentCount = attendances.stream()
                .filter(a -> a.getStatus() == Attendance.AttendanceStatus.PRESENT)
                .count();
        return (presentCount * 100.0) / attendances.size();
    }

    private double calculateAverageMarks(List<Marks> marks) {
        if (marks.isEmpty()) return 0;
        return marks.stream()
                .mapToDouble(Marks::getPercentage)
                .average()
                .orElse(0);
    }

    private String calculateGrade(double average) {
        if (average >= 90) return "A";
        if (average >= 80) return "B";
        if (average >= 70) return "C";
        if (average >= 60) return "D";
        return "F";
    }

    private String getPerformanceStatus(double averageMarks, double attendancePercentage) {
        if (averageMarks >= 80 && attendancePercentage >= 85) return "Excellent";
        if (averageMarks >= 70 && attendancePercentage >= 75) return "Good";
        if (averageMarks >= 60 && attendancePercentage >= 65) return "Average";
        return "Poor";
    }
}
