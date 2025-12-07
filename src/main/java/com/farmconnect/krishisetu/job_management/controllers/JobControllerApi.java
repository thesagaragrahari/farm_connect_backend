package com.farmconnect.krishisetu.job_management.controllers;


import com.farmconnect.krishisetu.job_management.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.farmconnect.krishisetu.job_management.model.JobProfile;

@RestController
@RequestMapping("/api/jobs")
public class JobControllerApi {
    @Autowired
    private JobService jobService;
    /*     * Job Management APIs
     */
    @GetMapping("/hello")
    public String helloJob(){
        return jobService.helloJob();
    };
    /* apis for farmers */

    // @PostMapping("farmer/postjob/{email}")
    // public ResponseEntity<?> createJob(@PathVariable String email, @RequestBody JobProfile jobProfile){
    //     return jobService.createJob(email,jobProfile );
    // }

    // @PostMapping("farmer/assign/job/{jobId}/{workerId}/{userType}")
    // public ResponseEntity<JobDetails> assignJobToWorker(@PathVariable Long jobId, @PathVariable Long workerId, @PathVariable String userType){
    //     return jobService.assignJobToWorker(jobId, workerId, userType);
    // }

    // @GetMapping("farmer/details/{jobId}")
    // public ResponseEntity<JobDetails> getJobDetails(@PathVariable Long jobId){
    //     return jobService.getJobDetails(jobId);
    // }

    // @GetMapping("farmer/jobs/active/{userId}/{userType}")
    // public ResponseEntity<List<JobDetails>> getActiveFarmerJobs(@PathVariable Long userId,   @PathVariable String userType){
    //     return jobService.getActiveFarmerJobs(userId, userType);
    // }

    // @GetMapping("farmer/jobs/completed/{userId}/{userType}")
    // public ResponseEntity<List<JobDetails>> getCompletedFarmerJobs(@PathVariable Long userId,   @PathVariable String userType){
    //     return jobService.getCompletedFarmerJobs(userId, userType);
    // }

    // @GetMapping("farmer/jobs/all/{userId}/{userType}")
    // public ResponseEntity<List<JobDetails>> getAllFarmerJobs(@PathVariable Long userId,   @PathVariable String userType){
    //     return jobService.getAllFarmerJobs(userId, userType);
    // }
    
    // @PostMapping("farmer/update/{jobId}")
    // public ResponseEntity<JobDetails> updateJobDetails(@PathVariable Long jobId, @RequestBody JobUpdateRequest request){
    //     return jobService.updateJobDetails(jobId, request);
    // }

    // /* apis for workers */

    // @PostMapping("workers/activejobs/{workerId}/{userType}")
    // public ResponseEntity<List<JobDetails>> searchJobs(@RequestBody JobSearchRequest request){
    //     return jobService.searchJobs(request);
    // }
    // @GetMapping("workers/jobs/assigned/{workerId}/{userType}")
    // public ResponseEntity<List<JobDetails>> getAssignedJobs(@PathVariable Long workerId,   @PathVariable String userType){
    //     return jobService.getAssignedJobs(workerId, userType);
    // }

    // @PostMapping("workers/update/status/{jobId}/{status}")
    // public ResponseEntity<JobDetails> updateJobStatus(@PathVariable Long jobId, @PathVariable String status){
    //     return jobService.updateJobStatus(jobId, status);
    // }

    // @PostMapping("workers/apply/{jobId}/{workerId}/{userType}")
    // public ResponseEntity<JobApplicationResponse> applyForJob(@PathVariable Long jobId, @PathVariable Long workerId, @PathVariable String userType){
    //     return jobService.applyForJob(jobId, workerId, userType);
    // }

    // @GetMapping("workers/jobdetails/{jobId}")
    // public ResponseEntity<JobDetails> getJobDetailsForWorker(@PathVariable Long jobId){
    //     return jobService.getJobDetailsForWorker(jobId);  
    // }


}

