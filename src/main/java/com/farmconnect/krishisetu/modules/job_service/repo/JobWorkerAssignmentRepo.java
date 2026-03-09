package com.farmconnect.krishisetu.modules.job_service.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.farmconnect.krishisetu.modules.job_service.entity.Job;
import com.farmconnect.krishisetu.modules.job_service.entity.JobWorkerAssignment;
import com.farmconnect.krishisetu.modules.job_service.enums.JobStatus;

import java.util.List;

@Repository
public interface JobWorkerAssignmentRepo extends JpaRepository<JobWorkerAssignment, Integer> {
    
}
