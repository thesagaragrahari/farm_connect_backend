package com.farmconnect.krishisetu.job_management.entity;

import com.farmconnect.krishisetu.users_management.entity.Farmer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job", schema = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Maps to gen_random_uuid()
    @Column(name = "job_id")
    private UUID jobId;

    @Column(name = "title", length = 255, nullable = false)
    private String title;
    
    @Column(name = "job_type", length = 255, nullable = false)
    private String jobType;
    
    @Column(name = "status", length = 255, nullable = false)
    private String status;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // --- FK 1: Farmer (Many-to-One) ---
    // REFERENCES users.farmers (farmer_id)
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "farmer_id")
    // private Farmer farmer;

    // --- FK 2: Job Location (Many-to-One) ---
    // REFERENCES jobs.job_location (job_location_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_location_id",nullable = true)
    private JobLocation jobLocation;

    // --- FK 3: Job Worker Assignment (Many-to-One / Non-standard) ---
    // REFERENCES jobs.job_worker_assignment (job_worker_assignment_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_worker_assignment_id",nullable =  true )
    private JobWorkerAssignment jobWorkerAssignment;
    
    // Note on ENUMs: The DDL uses custom PostgreSQL ENUM types 
    // (jobs.job_title_enum, jobs.job_type_enum, jobs.job_status_enum).
    // In Java, you would map these to String or custom Java Enums 
    // with a specific Hibernate type mapping to ensure correctness.
}