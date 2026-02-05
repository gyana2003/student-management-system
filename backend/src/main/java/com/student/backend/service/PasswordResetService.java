package com.student.backend.service;

import com.student.backend.model.User;
import com.student.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PasswordResetService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    
    // In-memory storage for reset codes (use database in production)
    private final Map<String, ResetCode> resetCodes = new HashMap<>();

    public PasswordResetService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public void requestPasswordReset(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new RuntimeException("Email not found");
        }

        // Generate a 6-character alphanumeric code
        String resetCode = generateResetCode();
        
        // Store reset code with expiration (valid for 30 minutes)
        resetCodes.put(email, new ResetCode(resetCode, System.currentTimeMillis() + 30 * 60 * 1000));

        // Send reset email
        sendResetEmail(email, resetCode);
    }

    public boolean verifyResetCode(String email, String resetCode) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new RuntimeException("Email not found");
        }

        ResetCode storedCode = resetCodes.get(email);
        if (storedCode == null) {
            throw new RuntimeException("No reset code found for this email");
        }

        // Check if code is expired
        if (storedCode.isExpired()) {
            resetCodes.remove(email);
            throw new RuntimeException("Reset code has expired");
        }

        // Verify code
        if (!storedCode.getCode().equals(resetCode.toUpperCase())) {
            throw new RuntimeException("Invalid reset code");
        }

        return true;
    }

    public void resetPassword(String email, String resetCode, String newPassword) {
        // Verify code first
        verifyResetCode(email, resetCode);

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new RuntimeException("Email not found");
        }

        // Update password
        User userObj = user.get();
        userObj.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userObj);

        // Remove reset code
        resetCodes.remove(email);
    }

    private String generateResetCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        return code.toString();
    }

    private void sendResetEmail(String email, String resetCode) {
        try {
            String subject = "Password Reset Code";
            String body = "Your password reset code is: " + resetCode + "\n" +
                         "This code will expire in 30 minutes.\n" +
                         "Do not share this code with anyone.";
            
            emailService.sendEmail(email, subject, body);
        } catch (Exception e) {
            System.out.println("Failed to send reset email: " + e.getMessage());
            // Continue with password reset process even if email fails
        }
    }

    // Inner class to store reset code with expiration
    private static class ResetCode {
        private final String code;
        private final long expirationTime;

        ResetCode(String code, long expirationTime) {
            this.code = code;
            this.expirationTime = expirationTime;
        }

        String getCode() {
            return code;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
    }
}
