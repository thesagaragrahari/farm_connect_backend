package com.farmconnect.krishisetu.modules.auth_service.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.farmconnect.krishisetu.modules.auth_service.entities.UserActionToken;
import com.farmconnect.krishisetu.modules.auth_service.entities.UserSecurityState;
import com.farmconnect.krishisetu.modules.auth_service.models.TokenType;
import com.farmconnect.krishisetu.modules.auth_service.models.TokenUtil;
import com.farmconnect.krishisetu.modules.auth_service.repositories.UserActionTokenRepository;
import com.farmconnect.krishisetu.modules.auth_service.repositories.UserSecurityStateRepository;
import com.farmconnect.krishisetu.modules.notification_service.event.NotificationEvent;
import com.farmconnect.krishisetu.modules.notification_service.models.NotificationType;
import com.farmconnect.krishisetu.modules.notification_service.producer.NotificationEventProducer;
import com.farmconnect.krishisetu.modules.user_service.DTOs.LoginReq;
import com.farmconnect.krishisetu.modules.user_service.entity.Farmer;
import com.farmconnect.krishisetu.modules.user_service.entity.User;
import com.farmconnect.krishisetu.modules.user_service.entity.Worker;
import com.farmconnect.krishisetu.modules.user_service.mapper.FarmerMapper;
import com.farmconnect.krishisetu.modules.user_service.mapper.UserMapper;
import com.farmconnect.krishisetu.modules.user_service.mapper.WorkerMapper;
import com.farmconnect.krishisetu.modules.user_service.model.FarmerProfile;
import com.farmconnect.krishisetu.modules.user_service.model.PointDTO;
import com.farmconnect.krishisetu.modules.user_service.model.UserProfile;
import com.farmconnect.krishisetu.modules.user_service.model.WorkerProfile;
import com.farmconnect.krishisetu.modules.user_service.repo.FarmerRepo;
import com.farmconnect.krishisetu.modules.user_service.repo.UserRepo;
import com.farmconnect.krishisetu.modules.user_service.repo.WorkerRepo;
import com.farmconnect.krishisetu.security.jwt.JwtUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger =
            LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final GeometryFactory geometryFactory;

    private final UserRepo userRepo;
    private final WorkerRepo workerRepo;
    private final FarmerRepo farmerRepo;

    private final UserMapper userMapper;
    private final WorkerMapper workerMapper;
    private final FarmerMapper farmerMapper;

    private final TokenUtil tokenUtil;
    private final ForgotPasswordRateLimiter rateLimiter;
    private final NotificationEventProducer notificationEventProducer;

    private final UserActionTokenRepository tokenRepo;
    private final UserSecurityStateRepository securityStateRepo;

    @Value("${app.backend.base-url:http://localhost:8080}")
    private String backendBaseUrl;

    @Value("${app.frontend.reset-password-url:http://localhost:3000/reset-password}")
    private String resetPasswordBaseUrl;

    /* =========================================================
                        REGISTER USER (ONLY BASE USER)
       ========================================================= */

    @Transactional
    public ResponseEntity<String> registerUser(UserProfile profile) {

        if (userRepo.existsByEmail(profile.getEmail()))
            return ResponseEntity.status(409).body("Email already registered");

        User user = userMapper.toUserEntity(profile);
        user.setPassword(passwordEncoder.encode(profile.getPassword()));

        if (profile.getLocation() != null) {
            PointDTO dto = profile.getLocation();
            Point point = geometryFactory.createPoint(
                    new Coordinate(dto.getLongitude(), dto.getLatitude()));
            point.setSRID(4326);
            user.setLocation(point);
        }

        userRepo.save(user);

        initSecurity(user);

        sendActionToken(user, TokenType.EMAIL_VERIFICATION);
        publishWelcomeMail(user);

        return ResponseEntity.ok("Registered successfully. Verify email.");
    }

    /* =========================================================
                        COMPLETE WORKER PROFILE
       ========================================================= */

    @Transactional
    public ResponseEntity<String> completeWorkerProfile(
            WorkerProfile workerProfile) {

        User user = getVerifiedUser(workerProfile.getUserProfile().getEmail());

        if (!"worker".equalsIgnoreCase(user.getRole()))
            return ResponseEntity.badRequest().body("Invalid role");

        if (workerRepo.existsByUser(user))
            return ResponseEntity.badRequest().body("Profile already completed");

        Worker worker = workerMapper.toWorkerEntity(workerProfile);
        worker.setUser(user);
        workerRepo.save(worker);

        markProfileCompleted(user.getUserId());

        return ResponseEntity.ok("Worker profile completed");
    }

    /* =========================================================
                        COMPLETE FARMER PROFILE
       ========================================================= */

    @Transactional
    public ResponseEntity<String> completeFarmerProfile(
            FarmerProfile farmerProfile) {

        User user = getVerifiedUser(farmerProfile.getUserProfile().getEmail());

        if (!"farmer".equalsIgnoreCase(user.getRole()))
            return ResponseEntity.badRequest().body("Invalid role");

        if (farmerRepo.existsByUser(user))
            return ResponseEntity.badRequest().body("Profile already completed");

        Farmer farmer = farmerMapper.toFarmerEntity(farmerProfile);
        farmer.setUser(user);
        farmerRepo.save(farmer);

        markProfileCompleted(user.getUserId());

        return ResponseEntity.ok("Farmer profile completed");
    }

    /* =========================================================
                            LOGIN
       ========================================================= */

    public ResponseEntity<?> authenticateUser(LoginReq loginRequest) {

        try {
                User user = userRepo.findByEmail(loginRequest.getEmail())
                    .orElseThrow();

                UserSecurityState state =
                    securityStateRepo.findByUserId(user.getUserId())
                            .orElseThrow();

                if (!state.isEmailVerified()){
                        sendActionToken(user, TokenType.EMAIL_VERIFICATION);
                return ResponseEntity.status(401)
                        .body("Email not verified Please verify your email. A new verification link has been sent.");
                }

                Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequest.getEmail(),
                                    loginRequest.getPassword()
                            )
                    );

                SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

            

            String jwt = jwtUtil.generateToken(
                    (UserDetails) authentication.getPrincipal()
            );

            return ResponseEntity.ok(
                    Map.of(
                            "token", jwt,
                            "profileCompleted", state.isProfileCompleted()
                    )
            );
        }
        catch (LockedException e) {
            return ResponseEntity.status(401)
                    .body("Account locked");
        }
        catch (BadCredentialsException e) {
            return ResponseEntity.status(401)
                    .body("Invalid credentials");
        }
    }

    /* =========================================================
                        FORGOT PASSWORD
       ========================================================= */

    @Transactional
    public void forgotPassword(String email) {

        rateLimiter.validate(email);

        userRepo.findByEmail(email)
                .ifPresent(user ->
                        sendActionToken(user, TokenType.PASSWORD_RESET)
                );
    }

    /* =========================================================
                        RESET PASSWORD
       ========================================================= */

    @Transactional
    public void resetPassword(String rawToken, String newPassword) {

        String hash = tokenUtil.hashToken(rawToken);

        UserActionToken token =
                tokenRepo.findByTokenHashAndTokenType(
                        hash,
                        TokenType.PASSWORD_RESET
                ).orElseThrow(() -> new RuntimeException("Invalid token"));

        validateToken(token);

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        token.setUsed(true);
        tokenRepo.save(token);
    }

    /* =========================================================
                        VERIFY EMAIL
       ========================================================= */

    @Transactional
    public void verifyEmail(String rawToken) {

        String hash = tokenUtil.hashToken(rawToken);

        UserActionToken token =
                tokenRepo.findByTokenHashAndTokenType(
                        hash,
                        TokenType.EMAIL_VERIFICATION
                ).orElseThrow(() -> new RuntimeException("Invalid token"));

        validateToken(token);

        UserSecurityState state =
                securityStateRepo.findByUserId(
                        token.getUser().getUserId()
                ).orElseThrow();

        state.setEmailVerified(true);
        securityStateRepo.save(state);

        token.setUsed(true);
        tokenRepo.save(token);
    }

    /* =========================================================
                    INTERNAL HELPERS
       ========================================================= */

    private void initSecurity(User user) {

        UserSecurityState state = new UserSecurityState();
        state.setUserId(user.getUserId());
        state.setEmailVerified(false);
        state.setAccountLocked(false);
        state.setProfileCompleted(false);
        state.setTokenVersion(0);

        securityStateRepo.save(state);
    }

    private User getVerifiedUser(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserSecurityState state =
                securityStateRepo.findByUserId(user.getUserId())
                        .orElseThrow();

        if (!state.isEmailVerified())
            throw new RuntimeException("Email not verified");

        return user;
    }

    private void markProfileCompleted(Long userId) {
        UserSecurityState state =
                securityStateRepo.findByUserId(userId)
                        .orElseThrow();
        state.setProfileCompleted(true);
        securityStateRepo.save(state);
    }

    private void validateToken(UserActionToken token) {

        if (token.isUsed())
            throw new RuntimeException("Token already used");

        if (token.getExpiryTime()
                .isBefore(LocalDateTime.now()))
            throw new RuntimeException("Token expired");
    }

    private void sendActionToken(User user, TokenType type) {

        String raw = tokenUtil.generateRawToken();

        UserActionToken token = new UserActionToken();
        token.setUser(user);
        token.setTokenHash(tokenUtil.hashToken(raw));
        token.setTokenType(type);
        token.setUsed(false);
        token.setExpiryTime(LocalDateTime.now().plusMinutes(15));

        tokenRepo.save(token);

        logger.info("Generated {} token for {}",
                type, user.getEmail());
        publishActionTokenNotification(user, type, raw);
    }

    private void publishActionTokenNotification(User user, TokenType type, String rawToken) {
        NotificationEvent event = new NotificationEvent();
        event.setRecipient(user.getEmail());

        switch (type) {
            case EMAIL_VERIFICATION -> {
                event.setType(NotificationType.EMAIL_VERIFICATION);
                event.setMetadata(
                        Map.of(
                                "verificationLink",
                                backendBaseUrl + "/api/auth/verify-email?token=" + rawToken
                        )
                );
            }
            case PASSWORD_RESET -> {
                event.setType(NotificationType.PASSWORD_RESET);
                event.setMetadata(
                        Map.of(
                                "resetLink",
                                resetPasswordBaseUrl + "?token=" + rawToken
                        )
                );
            }
            default -> {
                logger.warn("No notification mapping for token type {}", type);
                return;
            }
        }

        notificationEventProducer.publish(event);
    }

    private void publishWelcomeMail(User user) {
        NotificationEvent event = new NotificationEvent();
        event.setRecipient(user.getEmail());
        event.setType(NotificationType.WELCOME_EMAIL);
        event.setMetadata(
                Map.of(
                        "name",
                        user.getFullName() == null ? "User" : user.getFullName()
                )
        );
        notificationEventProducer.publish(event);
    }
}
