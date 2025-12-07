package com.farmconnect.krishisetu.job_management.entity;

import com.farmconnect.krishisetu.users_management.entity.Worker;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter; // Imports required for Lombok
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder; // Often useful for clean object creation

@Entity
@Table(name = "job_worker_assignment", schema = "jobs")
@Getter // Generates all standard getters
@Setter // Generates all standard setters
@NoArgsConstructor // Generates a no-argument constructor (required by JPA)
@AllArgsConstructor // Generates a constructor with all fields
@Builder // Provides a fluid way to create instances (JobWorkerAssignment.builder()...build())
public class JobWorkerAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_worker_assignment_seq")
    @SequenceGenerator(
        name = "job_worker_assignment_seq",
        sequenceName = "jobs.job_worker_assignment_id_seq",
        allocationSize = 1
    )
    @Column(name = "job_worker_assignment_id")
    private Integer jobWorkerAssignmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;

    @Column(name = "assignment_status", length = 50)
    private String assignmentStatus;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;
    
    // Note: No explicit getters/setters/constructors are needed!
}