package com.farmconnect.krishisetu.users_management.reqres;

import com.farmconnect.krishisetu.users_management.model.FarmerProfile;
import com.farmconnect.krishisetu.users_management.model.WorkerProfile;

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
