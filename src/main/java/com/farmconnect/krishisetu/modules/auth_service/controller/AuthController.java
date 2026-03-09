package com.farmconnect.krishisetu.modules.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.farmconnect.krishisetu.modules.auth_service.models.ForgotPasswordRequest;
import com.farmconnect.krishisetu.modules.auth_service.models.ResetPasswordRequest;
import com.farmconnect.krishisetu.modules.auth_service.service.AuthService;
import com.farmconnect.krishisetu.modules.user_service.DTOs.LoginReq;
import com.farmconnect.krishisetu.modules.user_service.model.FarmerProfile;
import com.farmconnect.krishisetu.modules.user_service.model.UserProfile;
import com.farmconnect.krishisetu.modules.user_service.model.WorkerProfile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /* ================= REGISTER ================= */

    @PostMapping("/register/user")
    public ResponseEntity<String> registerUser(
            @RequestBody UserProfile profile) {

        return authService.registerUser(profile);
    }

    @PostMapping("/complete-profile/worker")
    public ResponseEntity<String> registerWorker(
            @RequestBody WorkerProfile profile) {

        return authService.completeWorkerProfile(profile);
    }

    @PostMapping("/complete-profile/farmer")
    public ResponseEntity<String> registerFarmer(
            @RequestBody FarmerProfile profile) {

        return authService.completeFarmerProfile(profile);
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