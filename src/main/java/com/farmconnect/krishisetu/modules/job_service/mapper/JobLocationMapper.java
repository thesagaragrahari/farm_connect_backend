package com.farmconnect.krishisetu.modules.job_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.farmconnect.krishisetu.modules.job_service.entity.JobLocation;
import com.farmconnect.krishisetu.modules.job_service.model.JobLocationProfile;

@Mapper(componentModel = "spring")
public interface JobLocationMapper {

    @Mapping(target = "pointdto.longitude", expression = "java(jobLocation.getLocation() != null ? jobLocation.getLocation().getX() : null)")
    @Mapping(target = "pointdto.latitude", expression = "java(jobLocation.getLocation() != null ? jobLocation.getLocation().getY() : null)")
    JobLocationProfile toJobLocationModel(JobLocation jobLocation);

    // IGNORE POINT HERE → you set point manually in service
    @Mapping(target = "location", ignore = true)
    JobLocation toJobLocationEntity(JobLocationProfile dto);
}

// @Mapper(componentModel = "spring")
// public interface JobLocationMapper{

//     @Mapping(target = "pointdto.longitude", expression = "java(jobLocation.getLocation() != null ? jobLocation.getLocation().getY() : null)")
//     @Mapping(target = "pointdto.latitude", expression = "java(jobLocation.getLocation() != null ? jobLocation.getLocation().getX() : null)")
//     JobLocationProfile toJobLocationModel(JobLocation jobLocation );
    
//     @Mapping(target = "location", ignore = true)
//     JobLocation toJobLocationEntity(JobLocationProfile jobLocationProfile);
// }
