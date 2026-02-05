package com.student.backend.controller;

import com.student.backend.dto.*;
import com.student.backend.service.AuthService;
import com.student.backend.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println("Login attempt for user: " + loginRequest.getUsername());
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        System.out.println("Registration request for user: " + registerRequest.getUsername());
        AuthResponse response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            passwordResetService.requestPasswordReset(request.getEmail());
            return ResponseEntity.ok(new ApiResponse("Password reset code sent to your email"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<?> verifyResetCode(@Valid @RequestBody VerifyResetCodeRequest request) {
        try {
            passwordResetService.verifyResetCode(request.getEmail(), request.getResetCode());
            return ResponseEntity.ok(new ApiResponse("Code verified successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        try {
            passwordResetService.resetPassword(request.getEmail(), request.getResetCode(), request.getNewPassword());
            return ResponseEntity.ok(new ApiResponse("Password reset successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Error: " + e.getMessage()));
        }
    }
}
