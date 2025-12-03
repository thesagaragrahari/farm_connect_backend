package com.farmconnect.krishisetu.job_management.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.farmconnect.krishisetu.users_management.entity.Worker;

@Entity
@Table(
        name = "job_worker_assignment",
        schema = "jobs",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"job_id", "worker_id"})}
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobWorkerAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;

    @Column(name = "assignment_status", length = 50, nullable = false)
    private String assignmentStatus = "assigned";

    @Column(name = "assigned_at", columnDefinition = "timestamp default now()")
    private LocalDateTime assignedAt = LocalDateTime.now();
}
