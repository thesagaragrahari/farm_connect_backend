package com.farmconnect.krishisetu.job_management.mapper;

import org.mapstruct.Mapper;

import com.farmconnect.krishisetu.job_management.entity.JobWorkerAssignment;
import com.farmconnect.krishisetu.job_management.model.JobWorkerAssignmentProfile;


@Mapper(componentModel = "spring")
public interface JobWorkerAssignmentMapper{

    JobWorkerAssignmentProfile toJobWorkerAssignmentModel(JobWorkerAssignment jobWorkerAssignment );
    
    JobWorkerAssignment toJobWorkerAssignmentEntity(JobWorkerAssignmentProfile jobWorkerAssignmentProfile);
}
