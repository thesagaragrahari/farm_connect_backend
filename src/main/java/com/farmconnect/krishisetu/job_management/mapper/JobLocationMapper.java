package com.farmconnect.krishisetu.job_management.mapper;

import org.mapstruct.Mapper;

import com.farmconnect.krishisetu.job_management.entity.JobLocation;
import com.farmconnect.krishisetu.job_management.model.JobLocationProfile;


@Mapper(componentModel = "spring")
public interface JobLocationMapper{

    JobLocationProfile toJobLocationModel(JobLocation jobLocation );
    
    JobLocation toJobLocationEntity(JobLocationProfile jobLocationProfile);
}
