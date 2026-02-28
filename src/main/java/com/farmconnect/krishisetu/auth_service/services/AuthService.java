package com.farmconnect.krishisetu.auth_service.services;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.farmconnect.krishisetu.CommonUtility.Entities.UserActionToken;
import com.farmconnect.krishisetu.CommonUtility.Entities.UserSecurityState;
import com.farmconnect.krishisetu.CommonUtility.Models.PointDTO;
import com.farmconnect.krishisetu.CommonUtility.Repos.UserActionTokenRepository;
import com.farmconnect.krishisetu.CommonUtility.Repos.UserSecurityStateRepository;
import com.farmconnect.krishisetu.CommonUtility.Services.EmailEventProducer;
import com.farmconnect.krishisetu.CommonUtility.Services.ForgotPasswordRateLimiter;
import com.farmconnect.krishisetu.CommonUtility.Services.KafkaResetPasswordEmailProducer;
import com.farmconnect.krishisetu.CommonUtility.Util.TokenUtil;
import com.farmconnect.krishisetu.security.jwt.JwtUtil;

import com.farmconnect.krishisetu.users_management.entity.Farmer;
import com.farmconnect.krishisetu.users_management.entity.User;
import com.farmconnect.krishisetu.users_management.entity.Worker;

import com.farmconnect.krishisetu.users_management.enums.TokenType;

import com.farmconnect.krishisetu.users_management.mapper.FarmerMapper;
import com.farmconnect.krishisetu.users_management.mapper.UserMapper;
import com.farmconnect.krishisetu.users_management.mapper.WorkerMapper;

import com.farmconnect.krishisetu.users_management.model.FarmerProfile;
import com.farmconnect.krishisetu.users_management.model.UserProfile;
import com.farmconnect.krishisetu.users_management.model.WorkerProfile;

import com.farmconnect.krishisetu.users_management.repo.FarmerRepo;
import com.farmconnect.krishisetu.users_management.repo.UserRepo;
import com.farmconnect.krishisetu.users_management.repo.WorkerRepo;

import com.farmconnect.krishisetu.users_management.reqres.LoginReq;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

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

    private final UserActionTokenRepository tokenRepo;
    private final UserSecurityStateRepository securityStateRepo;

    private final EmailEventProducer emailProducer;

    /* =========================================================
                        REGISTER USER
       ========================================================= */

    @Transactional
    public ResponseEntity<String> registerUser(UserProfile profile) {

        if (userRepo.existsByEmail(profile.getEmail()))
            return ResponseEntity.status(409).body("Email already registered");

        User user = userMapper.toUserEntity(profile);
        user.setPassword(passwordEncoder.encode(profile.getPassword()));

        /*
         * Optional location mapping
         */
        if (profile.getLocation() != null) {
            PointDTO dto = profile.getLocation();

            Point point = geometryFactory.createPoint(
                    new Coordinate(dto.getLongitude(), dto.getLatitude())
            );
            point.setSRID(4326);

            user.setLocation(point);
        }

        userRepo.save(user);

        initSecurity(user);
        sendActionToken(user, TokenType.EMAIL_VERIFICATION);

        return ResponseEntity.ok("User registered successfully");
    }

    /* =========================================================
                        REGISTER WORKER
       ========================================================= */

    @Transactional
    public ResponseEntity<String> registerWorker(WorkerProfile workerProfile) {

        UserProfile u = workerProfile.getUserProfile();

        if (userRepo.existsByEmail(u.getEmail()))
            return ResponseEntity.status(409).body("Email already exists");

        /*
         * USER TABLE
         */
        User user = userMapper.toUserEntity(u);
        user.setPassword(passwordEncoder.encode(u.getPassword()));
        user.setRole("worker");
        userRepo.save(user);

        /*
         * WORKER TABLE
         */
        Worker worker = workerMapper.toWorkerEntity(workerProfile);
        worker.setUser(user);
        try{
        workerRepo.save(worker);
        }
         catch(Exception e){
            logger.error("Error saving worker: {}", e.getMessage());
            throw e;
        }

        initSecurity(user);
        sendActionToken(user, TokenType.EMAIL_VERIFICATION);

        logger.info("Worker registered: {}", user.getEmail());

        return ResponseEntity.ok("Worker registered successfully");
    }

    /* =========================================================
                        REGISTER FARMER
       ========================================================= */

    @Transactional
    public ResponseEntity<String> registerFarmer(FarmerProfile farmerProfile) {

        UserProfile u = farmerProfile.getUserProfile();

        if (userRepo.existsByEmail(u.getEmail()))
            return ResponseEntity.status(409).body("Email already exists");

        /*
         * USER TABLE
         */
        User user = userMapper.toUserEntity(u);
        user.setPassword(passwordEncoder.encode(u.getPassword()));
        user.setRole("farmer");
        userRepo.save(user);

        /*
         * FARMER TABLE
         */
        Farmer farmer = farmerMapper.toFarmerEntity(farmerProfile);
        farmer.setUser(user);
        farmerRepo.save(farmer);

        initSecurity(user);
        sendActionToken(user, TokenType.EMAIL_VERIFICATION);

        logger.info("Farmer registered: {}", user.getEmail());

        return ResponseEntity.ok("Farmer registered successfully");
    }

    /* =========================================================
                            LOGIN
       ========================================================= */

    public ResponseEntity<String> authenticateUser(LoginReq loginRequest) {

        try {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequest.getEmail(),
                                    loginRequest.getPassword()
                            )
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtil.generateToken(
                    (UserDetails) authentication.getPrincipal()
            );

            return ResponseEntity.ok(jwt);
        }

        /*
         * EMAIL NOT VERIFIED
         */
        catch (DisabledException e) {

            User user = userRepo.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            UserSecurityState state =
                    securityStateRepo.findByUserId(user.getUserId())
                            .orElseThrow();

            if (!state.isEmailVerified()) {
                sendActionToken(user, TokenType.EMAIL_VERIFICATION);
            }

            return ResponseEntity
                    .status(HttpServletResponse.SC_UNAUTHORIZED)
                    .body("Email not verified. Verification link resent.");
        }

        /*
         * ACCOUNT LOCKED
         */
        catch (LockedException e) {
            return ResponseEntity
                    .status(HttpServletResponse.SC_UNAUTHORIZED)
                    .body("Account is locked.");
        }

        /*
         * WRONG PASSWORD
         */
        catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpServletResponse.SC_UNAUTHORIZED)
                    .body("Invalid email or password.");
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
                        )
                        .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (token.isUsed())
            throw new RuntimeException("Token already used");

        if (token.getExpiryTime().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Token expired");

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
                        )
                        .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (token.isUsed())
            throw new RuntimeException("Token already used");

        if (token.getExpiryTime().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Token expired");

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
                    INTERNAL METHODS
       ========================================================= */

    private void initSecurity(User user) {

        UserSecurityState state = new UserSecurityState();
        state.setUserId(user.getUserId());
        state.setEmailVerified(false);
        state.setAccountLocked(false);
        state.setTokenVersion(0);

        securityStateRepo.save(state);
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

    String link;

    if (type == TokenType.EMAIL_VERIFICATION) {

        link = "http://localhost:8080/api/auth/verify-email?token=" + raw;
        emailProducer.sendVerificationEmail(user.getEmail(), link);

    } else if (type == TokenType.PASSWORD_RESET) {

        link = "http://localhost:3000/reset-password?token=" + raw;
        emailProducer.sendResetEmail(user.getEmail(), link);

    } else {
        throw new RuntimeException("Unsupported token type");
    }

    logger.info("Generated token for {} : {}", user.getEmail(), link);
}
}