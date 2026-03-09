package com.farmconnect.krishisetu.modules.job_service.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.farmconnect.krishisetu.modules.job_service.entity.Job;
import com.farmconnect.krishisetu.modules.job_service.entity.JobLocation;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobLocRepo extends JpaRepository<JobLocation, UUID> {
    
}
