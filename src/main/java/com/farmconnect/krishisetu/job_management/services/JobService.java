package com.farmconnect.krishisetu.job_management.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.farmconnect.krishisetu.job_management.model.JobProfile;
import com.farmconnect.krishisetu.job_management.repo.JobRepo;
import com.farmconnect.krishisetu.users_management.entity.User;
import com.farmconnect.krishisetu.users_management.enums.UserRole;
import com.farmconnect.krishisetu.users_management.repo.UserRepo;

@Service
public
class JobService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    JobRepo jobRepo;

    public String helloJob() {
        return "Hello from Job Service!";
    }

    public ResponseEntity<?> createJob(String email, JobProfile jobProfile) {
        if(email == null || email.isBlank() || email.isEmpty() || jobProfile == null){
            return ResponseEntity.status(404).build();
        }
        //validate email 
        User user = userRepo.findByEmail(email);
        if(user.getRole().equalsIgnoreCase((UserRole.FARMER).toString())){
            //create job logic
            
        }
        return ResponseEntity.status(404).build();
    }

    
}