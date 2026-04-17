package com.farmconnect.krishisetu.modules.auth_service.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.farmconnect.krishisetu.common.event.user.UserEvent;
import com.farmconnect.krishisetu.common.event.user.UserEventType;
import com.farmconnect.krishisetu.common.kafka.EventPublisher;
import com.farmconnect.krishisetu.common.kafka.KafkaTopics;
import com.farmconnect.krishisetu.modules.auth_service.DTOs.LoginReq;
import com.farmconnect.krishisetu.modules.auth_service.DTOs.RegisterReq;
import com.farmconnect.krishisetu.modules.auth_service.entities.UserActionToken;
import com.farmconnect.krishisetu.modules.auth_service.entities.UserSecurityState;
import com.farmconnect.krishisetu.modules.auth_service.models.TokenType;
import com.farmconnect.krishisetu.modules.auth_service.repositories.UserActionTokenRepository;
import com.farmconnect.krishisetu.modules.auth_service.repositories.UserSecurityStateRepository;
import com.farmconnect.krishisetu.modules.auth_service.utility.AuthServiceUtil;
import com.farmconnect.krishisetu.modules.auth_service.utility.TokenUtil;
import com.farmconnect.krishisetu.modules.user_service.entity.User;
import com.farmconnect.krishisetu.modules.user_service.mapper.UserMapper;
import com.farmconnect.krishisetu.modules.user_service.model.UserProfile;
import com.farmconnect.krishisetu.modules.user_service.repo.UserRepo;
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
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final TokenUtil tokenUtil;
    private final ForgotPasswordRateLimiter rateLimiter;
    private final UserActionTokenRepository tokenRepo;
    private final UserSecurityStateRepository securityStateRepo;

    @Autowired
    AuthServiceUtil serviceUtil;
    @Autowired
    EventPublisher eventPublisher;

    KafkaTemplate<String, Object> kafkaTemplate;
   

    /* =========================================================
                        REGISTER USER (ONLY BASE USER)
       ========================================================= */

    @Transactional
    public ResponseEntity<String> registerUser(RegisterReq registerReq) {

        if (userRepo.existsByEmail(registerReq.getEmail()))
            return ResponseEntity.status(409).body("Email already registered Kindly login or use another email");
        UserProfile userProfile = new UserProfile();
        userProfile.setFullName(registerReq.getName());
        userProfile.setEmail(registerReq.getEmail());
        userProfile.setRole(registerReq.getRole());
        userProfile.setPhone(null); // phone is optional and can be set later
        userProfile.setCreatedAt(LocalDateTime.now());
        userProfile.setUpdatedAt(null);
        User user = userMapper.toUserEntity(userProfile);
        user.setPassword(passwordEncoder.encode(registerReq.getPassword()));


        //kafkaTemplate.send("user.events", "test message");
        logger.info("Registering user with email: {}", registerReq.getEmail());
        /* 
        if (userProfile.getLocation() != null) {
            PointDTO dto = userProfile.getLocation();
            Point point = geometryFactory.createPoint(
                    new Coordinate(dto.getLongitude(), dto.getLatitude()));
            point.setSRID(4326);
            user.setLocation(point);
        } */
        userRepo.save(user);
        serviceUtil.initSecurity(user.getUserId());

        String token = serviceUtil.createActionToken(user.getUserId(), TokenType.REGISTRATION);
        
        UserEvent event = UserEvent.builder().eventType(UserEventType.REGISTERED_AND_VERIFIED)
                        .email(user.getEmail())
                        .name(user.getFullName())
                        .role(user.getRole().toString())
                        .userId(user.getUserId())
                        .token(token)
                        .build();

        logger.info("Publishing user registration event for email: {}", registerReq.getEmail());
        eventPublisher.publish(
                KafkaTopics.USER_EVENTS,
                event
        );
        
        logger.info("Published user registration event for email: {}", registerReq.getEmail());
        //publishWelcomeMail(user);
        return ResponseEntity.ok("Registered successfully. Verify email.");
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
                        UserEvent event = UserEvent.builder().eventType(UserEventType.EMAIL_VERIFICATION)
                        .email(user.getEmail())
                        .name(user.getFullName())
                        .role(user.getRole().toString())
                        .userId(user.getUserId())
                        .token(serviceUtil.createActionToken(user.getUserId(), TokenType.EMAIL_VERIFICATION))
                        .build();

                        logger.info("Login failed sent email for user email verification event for email: {}", user.getEmail());
                        eventPublisher.publish(
                        "user.events",
                        event
                        );
                       // serviceUtil.sendActionToken(user, TokenType.EMAIL_VERIFICATION);
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
        try{
                rateLimiter.validate(email);

                if(userRepo.existsByEmail(email)){
                        User user = userRepo.findByEmail(email).orElseThrow();
                        UserEvent event = UserEvent.builder().eventType(UserEventType.PASSWORD_RESET)
                                        .email(user.getEmail())
                                        .name(user.getFullName())
                                        .role(user.getRole().toString())
                                        .userId(user.getUserId())
                                        .token(serviceUtil.createActionToken(user.getUserId(), TokenType.PASSWORD_RESET))
                                        .build();

                        logger.info("Publishing password reset event for email: {}", email);
                        eventPublisher.publish(
                                KafkaTopics.USER_EVENTS,
                                event
                        );
                        logger.info("Published password reset event for email: {}", email);
                }
                // userRepo.findByEmail(email)
                //         .ifPresent(user ->
                //                 serviceUtil.createActionToken(user.getUserId(), TokenType.PASSWORD_RESET)
                //                 UserEvent event = UserEvent.builder().eventType(UserEventType.PASSWORDRESET)
                //                                 .email(user.getEmail())
                //                                 .name(user.getFullName())
                //                                 .role(user.getRole().toString())
                //                                 .userId(user.getUserId())
                //                                 .token(serviceUtil.createActionToken(user.getUserId(), TokenType.PASSWORD_RESET))
                //                                 .build();
                //         );
        }
        catch (RuntimeException e){
            // To prevent email enumeration we will not reveal if the email is registered or not
            logger.warn("Forgot password attempt for email: {} - {}", email, e.getMessage());
             // Optionally, you can publish an event for monitoring purposes
        }
        
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

        serviceUtil.validateToken(token);

        User user = userRepo.findByUserId(token.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);


        token.setUsed(true);
        tokenRepo.save(token);
        UserEvent event = UserEvent.builder().eventType(UserEventType.PASSWORD_CHANGED)
                                        .email(user.getEmail())
                                        .name(user.getFullName())
                                        .role(user.getRole().toString())
                                        .userId(user.getUserId())
                                        //.token(serviceUtil.createActionToken(user.getUserId(), TokenType.PASSWORD_RESET))
                                        .build();

                        logger.info("Password changed successfully: {}", user.getEmail());
                        eventPublisher.publish(
                                KafkaTopics.USER_EVENTS,
                                event
                        );
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

        serviceUtil.validateToken(token);

        UserSecurityState state =
                securityStateRepo.findByUserId(
                        token.getUserId()
                ).orElseThrow();

        state.setEmailVerified(true);
        securityStateRepo.save(state);

        token.setUsed(true);
        tokenRepo.save(token);
        eventPublisher.publish(
                KafkaTopics.USER_EVENTS,
                UserEvent.builder()
                        .eventType(UserEventType.EMAIL_VERIFIED)
                        .email(userRepo.findByUserId(token.getUserId()).orElseThrow().getEmail())
                        .userId(token.getUserId())
                        .build()
        );
    }

}
