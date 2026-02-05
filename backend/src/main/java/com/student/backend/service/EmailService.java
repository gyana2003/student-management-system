package com.student.backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendRegistrationEmail(String email, String username, String firstName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Welcome to Student Management System");
            message.setText("Dear " + firstName + ",\n\n" +
                    "Welcome to our Student Management System!\n" +
                    "Your account has been created successfully.\n" +
                    "Username: " + username + "\n\n" +
                    "Please login to your account and update your profile.\n\n" +
                    "Best regards,\n" +
                    "Student Management Team");
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send registration email: " + e.getMessage());
        }
    }

    public void sendAttendanceNotification(String email, String studentName, double percentage, String status) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Attendance Update - " + status);
            message.setText("Dear " + studentName + ",\n\n" +
                    "Your current attendance status:\n" +
                    "Attendance Percentage: " + String.format("%.2f", percentage) + "%\n" +
                    "Status: " + status + "\n\n" +
                    "Please ensure you maintain good attendance.\n\n" +
                    "Best regards,\n" +
                    "Student Management Team");
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send attendance email: " + e.getMessage());
        }
    }

    public void sendMarksNotification(String email, String studentName, double marks, String grade, String subject) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Marks Notification - " + subject);
            message.setText("Dear " + studentName + ",\n\n" +
                    "Your marks have been updated:\n" +
                    "Subject: " + subject + "\n" +
                    "Marks: " + String.format("%.2f", marks) + "%\n" +
                    "Grade: " + grade + "\n\n" +
                    "Keep up the good work!\n\n" +
                    "Best regards,\n" +
                    "Student Management Team");
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send marks email: " + e.getMessage());
        }
    }

    public void sendBulkNotification(String email, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send bulk email: " + e.getMessage());
        }
    }

    public void sendEmail(String email, String subject, String body) {
        sendBulkNotification(email, subject, body);
    }
}
