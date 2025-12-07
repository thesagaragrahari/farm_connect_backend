package com.farmconnect.krishisetu.job_management.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobProfile{

    private UUID jobId;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String title;
    private String jobType;
    private String status;
    
}