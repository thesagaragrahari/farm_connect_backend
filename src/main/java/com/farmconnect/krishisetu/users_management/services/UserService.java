package com.farmconnect.krishisetu.users_management.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.farmconnect.krishisetu.users_management.entity.Farmer;
import com.farmconnect.krishisetu.users_management.entity.User;
import com.farmconnect.krishisetu.users_management.entity.Worker;
import com.farmconnect.krishisetu.users_management.enums.UserRole;
import com.farmconnect.krishisetu.users_management.mapper.FarmerMapper;
import com.farmconnect.krishisetu.users_management.mapper.UserMapper;
import com.farmconnect.krishisetu.users_management.mapper.WorkerMapper;
import com.farmconnect.krishisetu.users_management.model.FarmerProfile;
import com.farmconnect.krishisetu.users_management.model.UserProfile;
import com.farmconnect.krishisetu.users_management.model.WorkerProfile;
import com.farmconnect.krishisetu.users_management.repo.FarmerRepo;
import com.farmconnect.krishisetu.users_management.repo.UserRepo;
import com.farmconnect.krishisetu.users_management.repo.WorkerRepo;
import com.farmconnect.krishisetu.users_management.reqres.UserRegistrationRequest;



@Service
public
class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    FarmerRepo farmerRepo;

    @Autowired
    WorkerRepo workerRepo;

    @Autowired
    UserMapper Umapper;

    @Autowired
    FarmerMapper Fmapper;

    @Autowired
    WorkerMapper Wmapper;

    private static final Logger logger = LoggerFactory.getLogger(Service.class);
    

    // Service methods implementation

    public String helloUser() {
        return "Hello, User!";
    }


    public ResponseEntity<?> getUserProfile(Long userId, String role) {
        if(role == null || role.isEmpty() || role.isBlank()|| !UserRole.isValid(role)) {
            return ResponseEntity.badRequest().body("Role cannot be null or empty");
        }
        if(userId == null || userId <= 0) {
            return ResponseEntity.badRequest().body("Invalid user ID");
        }

        User user = userRepo.findByUserIdAndRole(userId,role);

        if(user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        //UserProfile userProfile = Umapper.toUserModel(user);

        if(role.equalsIgnoreCase(UserRole.FARMER.name())) {
            Farmer farmer = farmerRepo.findByUserUserId(userId);
            if(farmer == null) {
                return ResponseEntity.status(404).body("Farmer profile not found");
            }
            FarmerProfile farmerProfile = Fmapper.toFarmerModel(farmer);
            logger.info("Farmer profile retrieved successfully for userId: " + userId);
            return ResponseEntity.ok(farmerProfile);
        }else if(role.equalsIgnoreCase(UserRole.WORKER.name())) {
            // Similar logic for WorkerProfile
            Worker worker = workerRepo.findByUserUserId(userId);
            if(worker == null) {
                return ResponseEntity.status(404).body("Worker profile not found");
            }   
            WorkerProfile workerProfile = Wmapper.toWorkerModel(worker);
            return ResponseEntity.ok(workerProfile);
        }
        
        logger.error("Invalid role specified: " + role);
        return ResponseEntity.status(400).body("Invalid role specified");
        
    }


    // public ResponseEntity<?> registerUser(String role, UserRegistrationRequest request) {
    //     if(role == null || role.isEmpty() || role.isBlank()|| !UserRole.isValid(role)) {
    //         return ResponseEntity.badRequest().body("Role cannot be null or empty");
    //     }
        

    //     return null;
    // }

    
    
}