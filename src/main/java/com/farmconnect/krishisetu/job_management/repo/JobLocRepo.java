package com.farmconnect.krishisetu.job_management.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.farmconnect.krishisetu.job_management.entity.Job;
import com.farmconnect.krishisetu.job_management.entity.JobLocation;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobLocRepo extends JpaRepository<JobLocation, UUID> {
    
}
