package com.farmconnect.krishisetu.job_management.mapper;



import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.farmconnect.krishisetu.job_management.entity.Job;
import com.farmconnect.krishisetu.job_management.model.JobProfile;


@Mapper(componentModel = "spring")
public interface JobMapper{

    @Mapping(source = "jobLocation", target = "jobLocationProfile")
    @Mapping(source = "jobWorkerAssignment", target = "jobWorkerAssignmentProfile")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "jobType", target = "jobType")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    JobProfile toJobModel(Job job);


    @Mapping(source = "jobLocationProfile", target = "jobLocation")
    @Mapping(source = "jobWorkerAssignmentProfile", target = "jobWorkerAssignment")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "jobType", target = "jobType")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    Job toJobEntity(JobProfile jobProfile);
}