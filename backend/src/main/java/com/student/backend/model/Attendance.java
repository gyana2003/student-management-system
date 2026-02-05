package com.student.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ATTENDANCE")
public class Attendance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    private String remarks;

    private String subject;

    // Default Constructor
    public Attendance() {
    }

    // All-args Constructor
    public Attendance(Long id, Student student, LocalDate attendanceDate, AttendanceStatus status,
                      String remarks, String subject) {
        this.id = id;
        this.student = student;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.remarks = remarks;
        this.subject = subject;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public enum AttendanceStatus {
        PRESENT,
        ABSENT,
        LATE,
        LEAVE
    }

    // Builder Pattern
    public static AttendanceBuilder builder() {
        return new AttendanceBuilder();
    }

    public static class AttendanceBuilder {
        private Long id;
        private Student student;
        private LocalDate attendanceDate;
        private AttendanceStatus status;
        private String remarks;
        private String subject;

        public AttendanceBuilder id(Long id) { this.id = id; return this; }
        public AttendanceBuilder student(Student student) { this.student = student; return this; }
        public AttendanceBuilder attendanceDate(LocalDate attendanceDate) { this.attendanceDate = attendanceDate; return this; }
        public AttendanceBuilder status(AttendanceStatus status) { this.status = status; return this; }
        public AttendanceBuilder remarks(String remarks) { this.remarks = remarks; return this; }
        public AttendanceBuilder subject(String subject) { this.subject = subject; return this; }

        public Attendance build() {
            return new Attendance(id, student, attendanceDate, status, remarks, subject);
        }
    }
}
