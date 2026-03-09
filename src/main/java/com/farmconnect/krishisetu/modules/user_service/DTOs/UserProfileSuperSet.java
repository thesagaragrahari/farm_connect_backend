package com.farmconnect.krishisetu.modules.user_service.DTOs;

import com.farmconnect.krishisetu.modules.user_service.model.FarmerProfile;
import com.farmconnect.krishisetu.modules.user_service.model.WorkerProfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileSuperSet {
    private FarmerProfile farmerProfile;
    private WorkerProfile workerProfile;
}
