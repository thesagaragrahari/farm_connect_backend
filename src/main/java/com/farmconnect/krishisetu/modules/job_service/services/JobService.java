package com.farmconnect.krishisetu.modules.job_service.services;



import com.farmconnect.krishisetu.config.GeometryConfig;
import com.farmconnect.krishisetu.modules.job_service.entity.Job;
import com.farmconnect.krishisetu.modules.job_service.entity.JobLocation;
import com.farmconnect.krishisetu.modules.job_service.mapper.JobMapper;
import com.farmconnect.krishisetu.modules.job_service.model.JobLocationProfile;
import com.farmconnect.krishisetu.modules.job_service.model.JobProfile;
import com.farmconnect.krishisetu.modules.job_service.repo.JobRepo;
import com.farmconnect.krishisetu.modules.user_service.entity.User;
import com.farmconnect.krishisetu.modules.user_service.enums.UserRole;
import com.farmconnect.krishisetu.modules.user_service.model.PointDTO;
import com.farmconnect.krishisetu.modules.user_service.repo.UserRepo;

import java.util.Optional;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public
class JobService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    JobRepo jobRepo;

    @Autowired
    JobMapper jMapper;

    @Autowired
    private GeometryFactory geometryFactory;

    public String helloJob() {
        return "Hello from Job Service!";
    }

    public ResponseEntity<String> createJob(JobProfile jobProfile) {
        if(jobProfile == null )
            return ResponseEntity.status(404).body("job details are not found");

        Job job = jMapper.toJobEntity(jobProfile);
        JobLocationProfile jobLocation = jobProfile.getJobLocationProfile();
        if (jobLocation.getLocation() != null) {
        PointDTO dto = jobLocation.getLocation();
        Point point = geometryFactory.createPoint(
                new Coordinate(dto.getLongitude(), dto.getLatitude())
        );
        point.setSRID(4326);
        job.getJobLocation().setLocation(point);
        }
        jobRepo.save(job);

        return ResponseEntity.status(201).body("Job created successfully");
    }
    

    
}