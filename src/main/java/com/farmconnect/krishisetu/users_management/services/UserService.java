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
import com.farmconnect.krishisetu.users_management.util.CommonUtililty;




@Service
public
class UserService {

    @Autowired
    CommonUtililty commonUtililty;


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
        UserProfile userProfile = Umapper.toUserModel(user);
        return ResponseEntity.ok(userProfile);
        // if(role.equalsIgnoreCase(UserRole.FARMER.name())) {
        //     Farmer farmer = farmerRepo.findByUserId(userId);
        //     if(farmer == null) {
        //         return ResponseEntity.status(404).body("Farmer profile not found");
        //     }
        //     FarmerProfile farmerProfile = new FarmerProfile();
        //     //farmerProfile = Fmapper.toFarmerModel(farmer);
        //     //farmerProfile.setUserProfile(userProfile);
        //     //farmerProfile.setLandArea(farmer.getLandAreaAcre());
        //     logger.info("Farmer profile retrieved successfully for userId: " + userId);
        //     return ResponseEntity.ok(farmerProfile);
            
        //  }

        // else if(role.equals(UserRole.WORKER.name())) {
        //     // Similar logic for WorkerProfile
        //     Worker worker = workerRepo.findByUserId(userId);
        //     if(worker == null) {
        //         return ResponseEntity.status(404).body("Worker profile not found");
        //     }   
        //     WorkerProfile workerProfile = new WorkerProfile();
        //     //Wmapper.toWorkerModel(worker);
        //     workerProfile.setUserProfile(userProfile);
        //     workerProfile.setDaily_wage(worker.getDailyWage());
        //     workerProfile.setExperience_years(worker.getExperienceYears());
        //     workerProfile.setJob_type(worker.getJobType());
        //     workerProfile.setMachine_id(worker.getMachineId());
        //     workerProfile.setSkill_set(commonUtililty.populateWorkerSkills(worker.getWorkerId()));
        //     workerProfile.setStatus(worker.getStatus());
        //     return ResponseEntity.ok(workerProfile);
        // }
        
        // logger.error("Invalid role specified: " + role);
        // return ResponseEntity.status(400).body("Invalid role specified");
    }

    
    
}