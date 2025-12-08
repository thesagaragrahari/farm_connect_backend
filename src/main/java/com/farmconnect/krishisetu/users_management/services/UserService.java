package com.farmconnect.krishisetu.users_management.services;


import java.util.List;

import org.hibernate.boot.registry.classloading.spi.ClassLoaderService.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.farmconnect.krishisetu.users_management.entity.Farmer;
import com.farmconnect.krishisetu.users_management.entity.Skill;
import com.farmconnect.krishisetu.users_management.entity.User;
import com.farmconnect.krishisetu.users_management.entity.Worker;
import com.farmconnect.krishisetu.users_management.enums.UserRole;
import com.farmconnect.krishisetu.users_management.enums.UserStatus;
import com.farmconnect.krishisetu.users_management.mapper.FarmerMapper;
import com.farmconnect.krishisetu.users_management.mapper.SkillMapper;
import com.farmconnect.krishisetu.users_management.mapper.UserMapper;
import com.farmconnect.krishisetu.users_management.mapper.WorkerMapper;
import com.farmconnect.krishisetu.users_management.model.FarmerProfile;
import com.farmconnect.krishisetu.users_management.model.SkillProfile;
import com.farmconnect.krishisetu.users_management.model.UserProfile;
import com.farmconnect.krishisetu.users_management.model.WorkerProfile;
import com.farmconnect.krishisetu.users_management.repo.FarmerRepo;
import com.farmconnect.krishisetu.users_management.repo.SkillRepo;
import com.farmconnect.krishisetu.users_management.repo.UserRepo;
import com.farmconnect.krishisetu.users_management.repo.WorkerRepo;
import com.farmconnect.krishisetu.users_management.reqres.UserProfileSuperSet;

import io.swagger.v3.oas.annotations.parameters.RequestBody;



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
    SkillRepo skillRepo;

    @Autowired
    UserMapper Umapper;

    @Autowired
    FarmerMapper Fmapper;

    @Autowired
    WorkerMapper Wmapper;

    @Autowired
    SkillMapper Smapper;

    
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

    // public ResponseEntity<?> updateUserProfile(String email, String role, UserProfileSuperSet request) {
    //     if(email == null || email.isEmpty() || email.isBlank() || role == null || role.isEmpty() || role.isBlank() || !UserRole.isValid(role)|| request == null) {
    //         return ResponseEntity.badRequest().body("Invalid input parameters");
    //     }
    //     if(role.equalsIgnoreCase(UserRole.FARMER.name())) {
    //         // Update Farmer Profile
    //         FarmerProfile farmerProfile = request.getFarmerProfile();
    //         if(farmerProfile == null) {
    //             return ResponseEntity.badRequest().body("Farmer profile data is missing");
    //         }
    //         // Fetch existing farmer
    //         Farmer existingFarmer = farmerRepo.findByUserEmail(email);
    //         if(existingFarmer == null) {
    //             return ResponseEntity.status(404).body("Farmer not found");
    //         }
    //         // Map updated fields
    //         Fmapper.updateFarmerEntityFromModel(farmerProfile, existingFarmer);
    //         farmerRepo.save(existingFarmer);
    //         FarmerProfile updatedProfile = Fmapper.toFarmerModel(existingFarmer);
    //         return ResponseEntity.ok(updatedProfile);
    //     } else if(role.equalsIgnoreCase(UserRole.WORKER.name())) {
    //         // Update Worker Profile
    //         WorkerProfile workerProfile = request.getWorkerProfile();
    //         if(workerProfile == null) {
    //             return ResponseEntity.badRequest().body("Worker profile data is missing");
    //         }
    //         // Fetch existing worker
    //         Worker existingWorker = workerRepo.findByUserEmail(email);
    //         if(existingWorker == null) {
    //             return ResponseEntity.status(404).body("Worker not found");
    //         }
    //         // Map updated fields
    //         Wmapper.updateWorkerEntityFromModel(workerProfile, existingWorker);
    //         workerRepo.save(existingWorker);
    //         WorkerProfile updatedProfile = Wmapper.toWorkerModel(existingWorker);
    //         return ResponseEntity.ok(updatedProfile);
    //     }
    //     return ResponseEntity.badRequest().body("Invalid role specified");
    // }


    // public ResponseEntity<?> registerUser(UserProfile request) {
    //     if(request == null) {
    //         return ResponseEntity.badRequest().body("Request body cannot be null");
    //     }
    //     if(request.getRole() == null || request.getRole().isEmpty() || request.getRole().isBlank()|| !UserRole.isValid(request.getRole())) {
    //         return ResponseEntity.badRequest().body("Role cannot be null or empty");
    //     }
    //     if(request.getFullName() == null || request.getFullName().isEmpty() || request.getFullName().isBlank()) {
    //         return ResponseEntity.badRequest().body("Name cannot be null or empty");
    //     }
    //     if(request.getEmail() == null || request.getEmail().isEmpty() || request.getEmail().isBlank()) {
    //         return ResponseEntity.badRequest().body("Email cannot be null or empty");
    //     } 
    //     if(request.getPhone() == null || request.getPhone().isEmpty() || request.getPhone().isBlank()) {
    //         return ResponseEntity.badRequest().body("Phone cannot be null or empty");
    //     }
    //     // already existing email check
    //     User existingUser = userRepo.findByEmail(request.getEmail());
    //     if(existingUser != null) {
    //         return ResponseEntity.status(409).body("Email already registered");
    //     }
    //     User newUser = Umapper.toUserEntity(request);
    //     userRepo.save(newUser);
    //     return ResponseEntity.ok("User registered successfully");
    // }



    // get workers by skills
    public ResponseEntity<List<WorkerProfile>> getWorkersBySkills(String email,List<String> skillReq) {
        if(email == null || email.isEmpty() || email.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        if(skillReq == null || skillReq.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        //validate skills
        List<Skill> validSkills = skillRepo.findBySkillNameIn(skillReq);
        if(validSkills == null || validSkills.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Integer> skillIds = validSkills.stream()
                    .map(Skill::getSkillId)
                    .toList();


        //List<SkillProfile> skillProfiles = Smapper.toSkillModelList(validSkills);
        List<Worker> workers = workerRepo.findWorkersHavingAnySkill(skillIds);
        if(workers == null || workers.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        List<WorkerProfile> workerProfiles = workers.stream()
        .map(Wmapper::toWorkerModel)
        .toList();
        return ResponseEntity.ok(workerProfiles);

    }


    public ResponseEntity<List<WorkerProfile>> getWorkersByJobType(String jobType) {
        // TODO Auto-generated method stub
        if(jobType == null || jobType.isEmpty() || jobType.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        List<Worker> workers = workerRepo.findByJobType(jobType);
        if(workers == null || workers.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        List<WorkerProfile> workerProfiles = workers.stream()
        .map(Wmapper::toWorkerModel)
        .toList();
        return ResponseEntity.ok(workerProfiles);

    }


    public ResponseEntity<List<WorkerProfile>> getActiveWorkers(String email) {
        if(email == null || email.isEmpty() || email.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        email = email.replaceAll("\s+", "").toLowerCase();

        List<Worker> workers = workerRepo.findByStatus(UserStatus.AVAILABLE.name().toLowerCase());
        if(workers == null || workers.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        List<WorkerProfile> workerProfiles = workers.stream()
        .map(Wmapper::toWorkerModel)
        .toList();
        return ResponseEntity.ok(workerProfiles);
    }


    // get skills of workers by email
    public ResponseEntity<List<SkillProfile>> getWorkersSkills(String email) {
        if(email == null || email.isEmpty() || email.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        email = email.replaceAll("\s+", "").toLowerCase();

        Worker worker = workerRepo.findByUserEmail(email);
        if(worker == null) {
            return ResponseEntity.status(404).build();
        }
        WorkerProfile workerProfile = Wmapper.toWorkerModel(worker);
        List<SkillProfile> skillsProfile = workerProfile.getSkillsProfile();
        if(skillsProfile == null || skillsProfile.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(skillsProfile);    
    }


    public ResponseEntity<WorkerProfile> updateWorkerStatus(String email, String status) {
        if(email == null || email.isEmpty() || email.isBlank() || status == null || status.isEmpty() || status.isBlank() || !UserStatus.isValid(status.toUpperCase())) {
            return ResponseEntity.badRequest().build();
        }

        email = email.replaceAll("\s+", "").toLowerCase();
        status = status.trim().toLowerCase();
        Worker worker = workerRepo.findByUserEmail(email);
        if(worker == null) {
            return ResponseEntity.status(404).build();
        }
        worker.setStatus(status);
        workerRepo.save(worker);
        WorkerProfile workerProfile = Wmapper.toWorkerModel(worker);
        return ResponseEntity.ok(workerProfile);
        }





    


    

    
    
}