package com.farmconnect.krishisetu.job_management.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import com.farmconnect.krishisetu.job_management.enums.JobStatus;
import com.farmconnect.krishisetu.job_management.enums.JobTitle;
import com.farmconnect.krishisetu.job_management.enums.JobType;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job", schema = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "job_id", updatable = false, nullable = false)
    private UUID jobId;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", columnDefinition = "timestamp default now()")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", columnDefinition = "timestamp default now()")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "title", nullable = false)
    private JobTitle title = JobTitle.PLOWING;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", nullable = false)
    private JobType jobType = JobType.ONE_TIME;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private JobStatus status = JobStatus.PENDING;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}




