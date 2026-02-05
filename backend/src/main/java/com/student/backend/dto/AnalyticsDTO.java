package com.student.backend.dto;

public class AnalyticsDTO {
    private Long studentId;
    private String studentName;
    private Double attendancePercentage;
    private Double averageMarks;
    private String overallGrade;
    private Integer totalSubjects;
    private Integer totalExams;
    private String performanceStatus;
    private Double attendanceToMarksCorrelation;

    // Default Constructor
    public AnalyticsDTO() {
    }

    // All-args Constructor
    public AnalyticsDTO(Long studentId, String studentName, Double attendancePercentage, Double averageMarks,
                       String overallGrade, Integer totalSubjects, Integer totalExams, String performanceStatus,
                       Double attendanceToMarksCorrelation) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.attendancePercentage = attendancePercentage;
        this.averageMarks = averageMarks;
        this.overallGrade = overallGrade;
        this.totalSubjects = totalSubjects;
        this.totalExams = totalExams;
        this.performanceStatus = performanceStatus;
        this.attendanceToMarksCorrelation = attendanceToMarksCorrelation;
    }

    // Getters and Setters
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(Double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public Double getAverageMarks() {
        return averageMarks;
    }

    public void setAverageMarks(Double averageMarks) {
        this.averageMarks = averageMarks;
    }

    public String getOverallGrade() {
        return overallGrade;
    }

    public void setOverallGrade(String overallGrade) {
        this.overallGrade = overallGrade;
    }

    public Integer getTotalSubjects() {
        return totalSubjects;
    }

    public void setTotalSubjects(Integer totalSubjects) {
        this.totalSubjects = totalSubjects;
    }

    public Integer getTotalExams() {
        return totalExams;
    }

    public void setTotalExams(Integer totalExams) {
        this.totalExams = totalExams;
    }

    public String getPerformanceStatus() {
        return performanceStatus;
    }

    public void setPerformanceStatus(String performanceStatus) {
        this.performanceStatus = performanceStatus;
    }

    public Double getAttendanceToMarksCorrelation() {
        return attendanceToMarksCorrelation;
    }

    public void setAttendanceToMarksCorrelation(Double attendanceToMarksCorrelation) {
        this.attendanceToMarksCorrelation = attendanceToMarksCorrelation;
    }
}
