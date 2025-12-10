package com.farmconnect.krishisetu.controller;

import com.farmconnect.krishisetu.security.jwt.JwtUtil;
import com.farmconnect.krishisetu.users_management.entity.Farmer;
import com.farmconnect.krishisetu.users_management.entity.Skill;
import com.farmconnect.krishisetu.users_management.entity.User;
import com.farmconnect.krishisetu.users_management.entity.Worker;
import com.farmconnect.krishisetu.users_management.enums.UserRole;
import com.farmconnect.krishisetu.users_management.mapper.FarmerMapper;
import com.farmconnect.krishisetu.users_management.mapper.UserMapper;
import com.farmconnect.krishisetu.users_management.mapper.WorkerMapper;
import com.farmconnect.krishisetu.users_management.model.FarmerProfile; // Assuming you have a LoginRequest DTO
import com.farmconnect.krishisetu.users_management.model.SkillProfile;
import com.farmconnect.krishisetu.users_management.model.UserProfile;
import com.farmconnect.krishisetu.users_management.model.WorkerProfile;
import com.farmconnect.krishisetu.users_management.repo.FarmerRepo;
import com.farmconnect.krishisetu.users_management.repo.SkillRepo;
import com.farmconnect.krishisetu.users_management.repo.UserRepo;
import com.farmconnect.krishisetu.users_management.repo.WorkerRepo;
import com.farmconnect.krishisetu.users_management.reqres.LoginReq;
import com.farmconnect.krishisetu.util.PointDTO;
import com.farmconnect.krishisetu.config.GeometryConfig;

import java.util.ArrayList;
import java.util.List;

//import org.hibernate.mapping.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // Matches the permitAll path in SecurityConfig
public class AuthController {


    private GeometryFactory geometryFactory;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder; 

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                      JwtUtil jwtUtil,
                      GeometryFactory geometryFactory,PasswordEncoder passwordEncoder) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
    this.geometryFactory = geometryFactory;
    this.passwordEncoder = passwordEncoder;
    }
  

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
    @Autowired
    SkillRepo skillRepo;


    // @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody LoginReq req) {

    // // Always pass phone here
    // Authentication authentication = authenticationManager.authenticate(
    //         new UsernamePasswordAuthenticationToken(req.getPhone(), req.getPassword())
    // );

    // SecurityContextHolder.getContext().setAuthentication(authentication);

    // UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    // String jwt = jwtUtil.generateToken(userDetails);

    // return ResponseEntity.ok(jwt);
    // }


    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginReq loginRequest) {
        
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

    // @PostMapping("register/user")
    // public ResponseEntity<String> registerUser(@RequestBody UserProfile uProfile){
    //     if(uProfile == null){
    //         return ResponseEntity.badRequest().build();
    //     }
    //     // if(!uProfile.getRole().equalsIgnoreCase((UserRole.WORKER).toString())){
    //     //     //register the user as a farmer
    //     //     return ResponseEntity.status(404).body("api mapping for register is wrong");
    //     // }
    //     if(uProfile.getRole() == null || uProfile.getRole().isBlank() || uProfile.getRole().isEmpty()){
    //         return ResponseEntity.badRequest().build();
    //     }
    //     if( uProfile.getEmail() == null  || uProfile.getEmail().isBlank() || uProfile.getEmail().isEmpty()){
    //         return ResponseEntity.badRequest().build();
    //     }
    //     if( uProfile.getPassword() == null  || uProfile.getPassword().isBlank() || uProfile.getPassword().isEmpty()){
    //         return ResponseEntity.badRequest().build();
    //     }
    //     if( uProfile.getFullName() == null  || uProfile.getFullName().isBlank() || uProfile.getFullName().isEmpty()){
    //         return ResponseEntity.badRequest().build();
    //     }
    //     User user = userRepo.findByEmail(uProfile.getEmail()).get();
    //     if(user != null ){
    //         return ResponseEntity.status(409).body("Email already registered");
    //     }

    //     User user1 = Umapper.toUserEntity(uProfile);
    //     PointDTO dto = uProfile.getLocation();
    //     if (dto.getLatitude() != 0 && dto.getLongitude() != 0) {
    //         Point point = geometryFactory.createPoint(new Coordinate(dto.getLongitude(), dto.getLatitude()));
    //         point.setSRID(4326);
    //         user1.setLocation(point);
    //     }
    //     userRepo.save(user1);
    //     return ResponseEntity.status(200).body("User Register succcessfully");    
    // }
    @PostMapping("/register/user")
    public ResponseEntity<String> registerUser(@RequestBody UserProfile profile) {

    if (profile == null) return ResponseEntity.badRequest().body("Invalid request");

    if (profile.getEmail() == null || profile.getEmail().isBlank())
        return ResponseEntity.badRequest().body("Email required");

    if (profile.getPassword() == null || profile.getPassword().isBlank())
        return ResponseEntity.badRequest().body("Password required");

    if (profile.getFullName() == null || profile.getFullName().isBlank())
        return ResponseEntity.badRequest().body("Full Name required");

    if (profile.getRole() == null || profile.getRole().isBlank())
        return ResponseEntity.badRequest().body("Role required");

    if (userRepo.existsByEmail(profile.getEmail()))
        return ResponseEntity.status(409).body("Email already registered");

    // MapStruct → Entity
    User user = Umapper.toUserEntity(profile);

    // Encode Password
    user.setPassword(passwordEncoder.encode(profile.getPassword()));

    // Location
    if (profile.getLocation() != null) {
        PointDTO dto = profile.getLocation();
        Point point = geometryFactory.createPoint(
                new Coordinate(dto.getLongitude(), dto.getLatitude())
        );
        point.setSRID(4326);
        user.setLocation(point);
    }

    userRepo.save(user);
    return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/register/worker")
    public ResponseEntity<String> registerWorker(@RequestBody WorkerProfile workerProfile) {

    UserProfile u = workerProfile.getUserProfile();

    if (u == null) return ResponseEntity.badRequest().body("User info missing");

    if (userRepo.existsByEmail(u.getEmail()))
        return ResponseEntity.status(409).body("Email already exists");

    // 1. Create USER first
    User user = Umapper.toUserEntity(u);
    user.setPassword(passwordEncoder.encode(u.getPassword()));
    userRepo.save(user);

    // List<Skill> sb = new ArrayList<Skill>();
    // sb = skillRepo.findBySkillNameIn(workerProfile.getSkills());


    // 2. Create WORKER
    Worker worker = Wmapper.toWorkerEntity(workerProfile);
    worker.setUser(user); // attach FK

    workerRepo.save(worker);

    return ResponseEntity.ok("Worker registered successfully");
    }


    @PostMapping("/register/farmer")
    public ResponseEntity<String> registerFarmer(@RequestBody FarmerProfile farmerProfile) {

    UserProfile u = farmerProfile.getUserProfile();

    if (userRepo.existsByEmail(u.getEmail()))
        return ResponseEntity.status(409).body("Email already exists");

    // 1. Create USER
    User user = Umapper.toUserEntity(u);
    user.setPassword(passwordEncoder.encode(u.getPassword()));
    userRepo.save(user);

    // 2. Create FARMER
    Farmer farmer = Fmapper.toFarmerEntity(farmerProfile);
    farmer.setUser(user);

    farmerRepo.save(farmer);

    return ResponseEntity.ok("Farmer registered successfully");
    }





    // @PostMapping("/register/worker")
    // public ResponseEntity<?> registerWorker(@RequestBody WorkerProfile workerProfile) {
        
    //     UserProfile uProfile = workerProfile.getUserProfile();

    //     //  if(!uProfile.getRole().equalsIgnoreCase((UserRole.WORKER).toString())){
    //     //     //register the user as a farmer
    //     //     return ResponseEntity.status(404).body("api mapping for register is wrong");
    //     // }
    //     if(uProfile.getRole() == null || uProfile.getRole().isBlank() || uProfile.getRole().isEmpty()){
    //         return ResponseEntity.badRequest().build();
    //     }
    //     if( uProfile.getEmail() == null  || uProfile.getEmail().isBlank() || uProfile.getEmail().isEmpty()){
    //         return ResponseEntity.badRequest().build();
    //     }
    //     if( uProfile.getPassword() == null  || uProfile.getPassword().isBlank() || uProfile.getPassword().isEmpty()){
    //         return ResponseEntity.badRequest().build();
    //     }
    //     if( uProfile.getFullName() == null  || uProfile.getFullName().isBlank() || uProfile.getFullName().isEmpty()){
    //         return ResponseEntity.badRequest().build();
    //     }
        
    //     if(workerRepo.findByUserEmail(uProfile.getEmail()) != null ){
    //         return ResponseEntity.status(409).body("Email already registered");
    //     }
        
    //     Worker worker = Wmapper.toWorkerEntity(workerProfile);
    //     workerRepo.save(worker);
    //     return ResponseEntity.status(200).body("User Register succcessfully");    
    // }
    
    // @PostMapping("/register/farmer")
    // public ResponseEntity<?> registerFarmer(@RequestBody FarmerProfile farmerProfile) {
    //             UserProfile uProfile = farmerProfile.getUserProfile();

    //     //  if(!uProfile.getRole().equalsIgnoreCase((UserRole.WORKER).toString())){
    //     //     //register the user as a farmer
    //     //     return ResponseEntity.status(404).body("api mapping for register is wrong");
    //     // }
    //     if(uProfile.getRole() == null || uProfile.getRole().isBlank() || uProfile.getRole().isEmpty()){
    //         return ResponseEntity.badRequest().build();
    //     }
    //     if( uProfile.getEmail() == null  || uProfile.getEmail().isBlank() || uProfile.getEmail().isEmpty()){
    //         return ResponseEntity.badRequest().build();
    //     }
    //     if( uProfile.getPassword() == null  || uProfile.getPassword().isBlank() || uProfile.getPassword().isEmpty()){
    //         return ResponseEntity.badRequest().build();
    //     }
    //     if( uProfile.getFullName() == null  || uProfile.getFullName().isBlank() || uProfile.getFullName().isEmpty()){
    //         return ResponseEntity.badRequest().build();
    //     }
        
    //     if(farmerRepo.findByUserEmail(uProfile.getEmail()) != null ){
    //         return ResponseEntity.status(409).body("Email already registered");
    //     }
        
    //     Farmer farmer = Fmapper.toFarmerEntity(farmerProfile);
    //     farmerRepo.save(farmer);
    //     return ResponseEntity.status(200).body("User Register succcessfully"); 
    // }
}