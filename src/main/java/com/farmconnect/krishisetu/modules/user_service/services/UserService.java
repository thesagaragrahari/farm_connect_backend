package com.farmconnect.krishisetu.modules.user_service.services;


import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.boot.registry.classloading.spi.ClassLoaderService.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.farmconnect.krishisetu.modules.auth_service.entities.UserSecurityState;
import com.farmconnect.krishisetu.modules.auth_service.repositories.UserSecurityStateRepository;
import com.farmconnect.krishisetu.modules.user_service.DTOs.UserProfileSuperSet;
import com.farmconnect.krishisetu.modules.user_service.entity.Farmer;
import com.farmconnect.krishisetu.modules.user_service.entity.Skill;
import com.farmconnect.krishisetu.modules.user_service.entity.User;
import com.farmconnect.krishisetu.modules.user_service.entity.Worker;
import com.farmconnect.krishisetu.modules.user_service.enums.UserRole;
import com.farmconnect.krishisetu.modules.user_service.enums.UserStatus;
import com.farmconnect.krishisetu.modules.user_service.mapper.FarmerMapper;
import com.farmconnect.krishisetu.modules.user_service.mapper.SkillMapper;
import com.farmconnect.krishisetu.modules.user_service.mapper.UserMapper;
import com.farmconnect.krishisetu.modules.user_service.mapper.WorkerMapper;
import com.farmconnect.krishisetu.modules.user_service.model.FarmerProfile;
import com.farmconnect.krishisetu.modules.user_service.model.SkillProfile;
import com.farmconnect.krishisetu.modules.user_service.model.UserProfile;
import com.farmconnect.krishisetu.modules.user_service.model.WorkerProfile;
import com.farmconnect.krishisetu.modules.user_service.repo.FarmerRepo;
import com.farmconnect.krishisetu.modules.user_service.repo.SkillRepo;
import com.farmconnect.krishisetu.modules.user_service.repo.UserRepo;
import com.farmconnect.krishisetu.modules.user_service.repo.WorkerRepo;

import org.springframework.security.crypto.password.PasswordEncoder;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.transaction.Transactional;



@Service
public
class UserService {


    private PasswordEncoder passwordEncoder;

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
    FarmerMapper farmerMapper;

    @Autowired
    WorkerMapper workerMapper;

    @Autowired
    SkillMapper Smapper;

    UserSecurityStateRepository securityStateRepo;

    
    private static final Logger logger = LoggerFactory.getLogger(Service.class);
    //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
    public String helloUser() {
        return "Hello, User!";
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


    public ResponseEntity<?> getUserProfile(org.springframework.security.core.userdetails.User springUser) {
        String email = springUser.getUsername();//it returns email as username

        List<String> roles = springUser.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());//EXTRACTING ROLES s

        if(roles.contains("ROLE_FARMER") && email != null) {
            Farmer farmer = farmerRepo.findByUserEmail(email);
            if(farmer == null) {
                return ResponseEntity.status(404).body("Farmer profile not found");
            }
            FarmerProfile farmerProfile = farmerMapper.toFarmerModel(farmer);
            logger.info("Farmer profile retrieved successfully for user: " + farmer);
            return ResponseEntity.ok(farmerProfile);
        }else if(roles.contains("ROLE_WORKER") && email != null) {
            // Similar logic for WorkerProfile
            Worker worker = workerRepo.findByUserEmail(email).orElse(null);
            if(worker == null) {
                return ResponseEntity.status(404).body("Worker profile not found");
            }   
            WorkerProfile workerProfile = workerMapper.toWorkerModel(worker);
            logger.info("Worker profile retrieved successfully for user: " + worker);
            return ResponseEntity.ok(workerProfile);
        }
        
        logger.error("Invalid role specified: " + roles);
        return ResponseEntity.status(400).body("Invalid role specified");
    }


    public ResponseEntity<String> updatePassword(org.springframework.security.core.userdetails.User springUser, String newPassword) {
        // TODO Auto-generated method stub
        User user = userRepo.findByEmail(springUser.getUsername()).orElse(null);
        if(user == null || user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank() 
            || newPassword == null || newPassword.isEmpty() || newPassword.isBlank()) {
            return ResponseEntity.status(404).body("User not found or invalid new password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        return ResponseEntity.status(200).body("Password updated successfully");
    }





    // get workers by skills
    public ResponseEntity<List<WorkerProfile>> getWorkersBySkills(List<String> skillReq) {
        if(skillReq == null || skillReq.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        List<Skill> validSkills = skillRepo.findBySkillNameIn(skillReq);
        if(validSkills == null || validSkills.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Integer> skillIds = validSkills.stream()
                    .map(Skill::getSkillId)
                    .toList();
        List<Worker> workers = workerRepo.findWorkersHavingAnySkill(skillIds);
        if(workers == null || workers.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        List<WorkerProfile> workerProfiles = workers.stream()
        .map(workerMapper::toWorkerModel)
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
        .map(workerMapper::toWorkerModel)
        .toList();
        return ResponseEntity.ok(workerProfiles);

    }


    public ResponseEntity<List<WorkerProfile>> getActiveWorkers() {
        List<Worker> workers = workerRepo.findByStatus(UserStatus.AVAILABLE.name().toLowerCase());
        if(workers == null || workers.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        List<WorkerProfile> workerProfiles = workers.stream()
        .map(workerMapper::toWorkerModel)
        .toList();
        return ResponseEntity.ok(workerProfiles);
    }


    // get skills of workers by email
    public ResponseEntity<List<SkillProfile>> getWorkersSkills(String email) {
        if(email == null || email.isEmpty() || email.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        email = email.replaceAll("\s+", "").toLowerCase();

        Worker worker = workerRepo.findByUserEmail(email).orElse(null);
        if(worker == null) {
            return ResponseEntity.status(404).build();
        }
        WorkerProfile workerProfile = workerMapper.toWorkerModel(worker);
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
        Worker worker = workerRepo.findByUserEmail(email).orElse(null);
        if(worker == null) {
            return ResponseEntity.status(404).build();
        }
        worker.setStatus(status);
        workerRepo.save(worker);
        WorkerProfile workerProfile = workerMapper.toWorkerModel(worker);
        return ResponseEntity.ok(workerProfile);
    }


    public ResponseEntity<?> updateFarmerProfile(org.springframework.security.core.userdetails.User springUser,
            FarmerProfile updatedProfile) {
            FarmerProfile existingProfile = farmerRepo.findByUserEmail(springUser.getUsername()).orElse(null);
            if(existingProfile == null) {
                return ResponseEntity.status(404).body("Farmer profile not found");
            }

            // // Update fields
            // Fmapper.updateFarmerEntityFromModel(updatedProfile, existingProfile);
            // farmerRepo.save(existingProfile);
            // FarmerProfile savedProfile = Fmapper.toFarmerModel(existingProfile);
            //return ResponseEntity.ok(savedProfile);
        throw new UnsupportedOperationException("Unimplemented method 'updateFarmerProfile'");
    }


    public ResponseEntity<?> updateUserProfile(org.springframework.security.core.userdetails.User springUser,
        UserProfile uProfile) {
        User existingUser = userRepo.findByEmail(springUser.getUsername()).orElse(null);
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUserProfile'");
    }





    


    /* =========================================================
                        HELPER METHODS
       ========================================================= */
    
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

    
    
}