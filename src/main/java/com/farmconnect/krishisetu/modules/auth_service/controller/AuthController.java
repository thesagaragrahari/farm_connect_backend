package com.farmconnect.krishisetu.modules.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import com.farmconnect.krishisetu.modules.auth_service.DTOs.LoginReq;
import com.farmconnect.krishisetu.modules.auth_service.DTOs.RegisterReq;
import com.farmconnect.krishisetu.modules.auth_service.models.ForgotPasswordRequest;
import com.farmconnect.krishisetu.modules.auth_service.models.ResetPasswordRequest;
import com.farmconnect.krishisetu.modules.auth_service.service.AuthService;
import com.farmconnect.krishisetu.modules.user_service.model.UserProfile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JavaMailSender mailSender;

    @GetMapping("/email")
    public String sendTestMail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("infosagaragrahari@gmail.com"); // your email
            message.setSubject("Test Email");
            message.setText("If you received this, email config is working!");

            mailSender.send(message);

            return "✅ Email sent successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Email failed: " + e.getMessage();
        }
    }

    /* ================= REGISTER ================= */

    @PostMapping("/register/user")
    public ResponseEntity<String> registerUser(
            @RequestBody RegisterReq registerReq) {

        return authService.registerUser(registerReq);
    }   

    /* ================= LOGIN ================= */

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginReq request) {

        return authService.authenticateUser(request);
    }

    /* ================= VERIFY EMAIL ================= */

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(
            @RequestParam String token) {

        authService.verifyEmail(token);
        return ResponseEntity.ok("Email verified successfully");
    }

    /* ================= FORGOT PASSWORD ================= */

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok("Reset link sent");
    }

    /* ================= RESET PASSWORD ================= */

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        authService.resetPassword(
                request.getToken(),
                request.getNewPassword()
        );
        return ResponseEntity.ok("Password reset successful");
    }
}