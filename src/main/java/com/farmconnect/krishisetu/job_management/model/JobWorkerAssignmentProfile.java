package com.farmconnect.krishisetu.job_management.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobWorkerAssignmentProfile {
    
    private Integer id;
    private UUID jobId;
    private Integer workerId;
    private Integer farmerId;
    private String assignmentStatus;
    private LocalDateTime assignedAt;
}
