package com.farmconnect.krishisetu.job_management.repo;


import com.farmconnect.krishisetu.job_management.entity.Job;
import com.farmconnect.krishisetu.job_management.entity.JobWorkerAssignment;
import com.farmconnect.krishisetu.job_management.enums.JobStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobWorkerAssignmentRepo extends JpaRepository<JobWorkerAssignment, Integer> {
    
}
