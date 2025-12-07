package com.farmconnect.krishisetu.job_management.model;

import com.farmconnect.krishisetu.users_management.model.FarmerProfile;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobProfile {

    // --- Job Core Fields ---
    private UUID jobId;
    private String title;
    private String jobType; // Maps to jobs.job_type_enum in DDL
    private String status;  // Maps to jobs.job_status_enum in DDL
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // --- Related Entity Profiles (Simplified Views) ---
    
    // FK 1: Farmer
    private FarmerProfile farmerProfile; 
    
    // FK 2: Job Location
    private JobLocationProfile jobLocationProfile;
    
    // FK 3: Job Worker Assignment
    private JobWorkerAssignmentProfile jobWorkerAssignmentProfile;

    // Note: If you have a One-to-Many relationship (e.g., many assignments per job),
    // this field would be a List<JobWorkerAssignmentProfile>.
}