package com.farmconnect.krishisetu.controller;

import com.farmconnect.krishisetu.security.jwt.JwtUtil;
import com.farmconnect.krishisetu.users_management.entity.Farmer;
import com.farmconnect.krishisetu.users_management.entity.User;
import com.farmconnect.krishisetu.users_management.entity.Worker;
import com.farmconnect.krishisetu.users_management.enums.UserRole;
import com.farmconnect.krishisetu.users_management.mapper.FarmerMapper;
import com.farmconnect.krishisetu.users_management.mapper.UserMapper;
import com.farmconnect.krishisetu.users_management.mapper.WorkerMapper;
import com.farmconnect.krishisetu.users_management.model.FarmerProfile; // Assuming you have a LoginRequest DTO
import com.farmconnect.krishisetu.users_management.model.UserProfile;
import com.farmconnect.krishisetu.users_management.model.WorkerProfile;
import com.farmconnect.krishisetu.users_management.repo.FarmerRepo;
import com.farmconnect.krishisetu.users_management.repo.UserRepo;
import com.farmconnect.krishisetu.users_management.repo.WorkerRepo;
import com.farmconnect.krishisetu.users_management.reqres.LoginReq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // Matches the permitAll path in SecurityConfig
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    WorkerRepo workerRepo;
    @Autowired
    WorkerMapper Wmapper;

    @Autowired
    FarmerRepo farmerRepo;
    @Autowired
    FarmerMapper Fmapper;

    @Autowired
    UserRepo userRepo;
    @Autowired
    UserMapper Umapper;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginReq loginRequest) {
        
        // 1. Authenticate user using Spring Security's mechanism
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 2. Generate JWT token
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails);

        // 3. Return the token to the client
        return ResponseEntity.ok(jwt); // Client receives the JWT string
    }

    @PostMapping("register/user")
    public ResponseEntity<String> registerUser(@RequestBody UserProfile uProfile){
        if(uProfile == null){
            return ResponseEntity.badRequest().build();
        }
        if(!uProfile.getRole().equalsIgnoreCase((UserRole.WORKER).toString())){
            //register the user as a farmer
            return ResponseEntity.status(404).body("api mapping for register is wrong");
        }
        if(uProfile.getRole() == null || uProfile.getRole().isBlank() || uProfile.getRole().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        if( uProfile.getEmail() == null  || uProfile.getEmail().isBlank() || uProfile.getEmail().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        if( uProfile.getPassword() == null  || uProfile.getPassword().isBlank() || uProfile.getPassword().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        if( uProfile.getFullName() == null  || uProfile.getFullName().isBlank() || uProfile.getFullName().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        
        if(workerRepo.findByUserEmail(uProfile.getEmail()) != null ){
            return ResponseEntity.status(409).body("Email already registered");
        }
        
        User user = Umapper.toUserEntity(uProfile);
        userRepo.save(user);
        return ResponseEntity.status(200).body("User Register succcessfully");    
    }

    @PostMapping("/register/worker")
    public ResponseEntity<?> registerWorker(@RequestBody WorkerProfile workerProfile) {
        
        UserProfile uProfile = workerProfile.getUserProfile();

         if(!uProfile.getRole().equalsIgnoreCase((UserRole.WORKER).toString())){
            //register the user as a farmer
            return ResponseEntity.status(404).body("api mapping for register is wrong");
        }
        if(uProfile.getRole() == null || uProfile.getRole().isBlank() || uProfile.getRole().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        if( uProfile.getEmail() == null  || uProfile.getEmail().isBlank() || uProfile.getEmail().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        if( uProfile.getPassword() == null  || uProfile.getPassword().isBlank() || uProfile.getPassword().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        if( uProfile.getFullName() == null  || uProfile.getFullName().isBlank() || uProfile.getFullName().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        
        if(workerRepo.findByUserEmail(uProfile.getEmail()) != null ){
            return ResponseEntity.status(409).body("Email already registered");
        }
        
        Worker worker = Wmapper.toWorkerEntity(workerProfile);
        workerRepo.save(worker);
        return ResponseEntity.status(200).body("User Register succcessfully");    
    }
    
    @PostMapping("/register/farmer")
    public ResponseEntity<?> registerFarmer(@RequestBody FarmerProfile farmerProfile) {
                UserProfile uProfile = farmerProfile.getUserProfile();

         if(!uProfile.getRole().equalsIgnoreCase((UserRole.WORKER).toString())){
            //register the user as a farmer
            return ResponseEntity.status(404).body("api mapping for register is wrong");
        }
        if(uProfile.getRole() == null || uProfile.getRole().isBlank() || uProfile.getRole().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        if( uProfile.getEmail() == null  || uProfile.getEmail().isBlank() || uProfile.getEmail().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        if( uProfile.getPassword() == null  || uProfile.getPassword().isBlank() || uProfile.getPassword().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        if( uProfile.getFullName() == null  || uProfile.getFullName().isBlank() || uProfile.getFullName().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        
        if(farmerRepo.findByUserEmail(uProfile.getEmail()) != null ){
            return ResponseEntity.status(409).body("Email already registered");
        }
        
        Farmer farmer = Fmapper.toFarmerEntity(farmerProfile);
        farmerRepo.save(farmer);
        return ResponseEntity.status(200).body("User Register succcessfully"); 
    }
}