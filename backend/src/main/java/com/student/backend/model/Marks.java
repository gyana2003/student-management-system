package com.student.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MARKS")
public class Marks {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private String subject;
    private String examType;
    private double marks;
    private double totalMarks;
    private LocalDateTime examDate;
    private String remarks;
    private double percentage;
    private String grade;

    // Default Constructor
    public Marks() {
    }

    // All-args Constructor
    public Marks(Long id, Student student, String subject, String examType, double marks, double totalMarks,
                 LocalDateTime examDate, String remarks, double percentage, String grade) {
        this.id = id;
        this.student = student;
        this.subject = subject;
        this.examType = examType;
        this.marks = marks;
        this.totalMarks = totalMarks;
        this.examDate = examDate;
        this.remarks = remarks;
        this.percentage = percentage;
        this.grade = grade;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public double getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(double totalMarks) {
        this.totalMarks = totalMarks;
    }

    public LocalDateTime getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDateTime examDate) {
        this.examDate = examDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @PrePersist
    @PreUpdate
    private void calculateGradeAndPercentage() {
        this.percentage = (marks / totalMarks) * 100;
        
        if (percentage >= 90) {
            this.grade = "A";
        } else if (percentage >= 80) {
            this.grade = "B";
        } else if (percentage >= 70) {
            this.grade = "C";
        } else if (percentage >= 60) {
            this.grade = "D";
        } else {
            this.grade = "F";
        }
    }

    // Builder Pattern
    public static MarksBuilder builder() {
        return new MarksBuilder();
    }

    public static class MarksBuilder {
        private Long id;
        private Student student;
        private String subject;
        private String examType;
        private double marks;
        private double totalMarks;
        private LocalDateTime examDate;
        private String remarks;
        private double percentage;
        private String grade;

        public MarksBuilder id(Long id) { this.id = id; return this; }
        public MarksBuilder student(Student student) { this.student = student; return this; }
        public MarksBuilder subject(String subject) { this.subject = subject; return this; }
        public MarksBuilder examType(String examType) { this.examType = examType; return this; }
        public MarksBuilder marks(double marks) { this.marks = marks; return this; }
        public MarksBuilder totalMarks(double totalMarks) { this.totalMarks = totalMarks; return this; }
        public MarksBuilder examDate(LocalDateTime examDate) { this.examDate = examDate; return this; }
        public MarksBuilder remarks(String remarks) { this.remarks = remarks; return this; }
        public MarksBuilder percentage(double percentage) { this.percentage = percentage; return this; }
        public MarksBuilder grade(String grade) { this.grade = grade; return this; }

        public Marks build() {
            return new Marks(id, student, subject, examType, marks, totalMarks, examDate, remarks, percentage, grade);
        }
    }
}
