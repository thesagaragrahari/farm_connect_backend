package com.farmconnect.krishisetu.users_management.reqres;

import org.springframework.stereotype.Component;

import com.farmconnect.krishisetu.users_management.model.FarmerProfile;
import com.farmconnect.krishisetu.users_management.model.UserProfile;
import com.farmconnect.krishisetu.users_management.model.WorkerProfile;

import lombok.Data;

@Data
@Component
public class UserRegistrationRequest {
    
    UserProfile userProfile;
    FarmerProfile farmerProfile;
    WorkerProfile workerProfile;

}
