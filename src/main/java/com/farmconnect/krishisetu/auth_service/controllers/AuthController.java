package com.farmconnect.krishisetu.auth_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.farmconnect.krishisetu.CommonUtility.Models.ForgotPasswordRequest;
import com.farmconnect.krishisetu.CommonUtility.Models.ResetPasswordRequest;
import com.farmconnect.krishisetu.auth_service.services.AuthService;
import com.farmconnect.krishisetu.users_management.model.FarmerProfile;
import com.farmconnect.krishisetu.users_management.model.UserProfile;
import com.farmconnect.krishisetu.users_management.model.WorkerProfile;
import com.farmconnect.krishisetu.users_management.reqres.LoginReq;

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

    @PostMapping("/register/worker")
    public ResponseEntity<String> registerWorker(
            @RequestBody WorkerProfile profile) {

        return authService.registerWorker(profile);
    }

    @PostMapping("/register/farmer")
    public ResponseEntity<String> registerFarmer(
            @RequestBody FarmerProfile profile) {

        return authService.registerFarmer(profile);
    }

    /* ================= LOGIN ================= */

    @PostMapping("/login")
    public ResponseEntity<String> login(
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