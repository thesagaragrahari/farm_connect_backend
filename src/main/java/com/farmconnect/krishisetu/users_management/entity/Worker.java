package com.farmconnect.krishisetu.users_management.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "workers", schema = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "worker_id", nullable = false)
    private Integer workerId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "daily_wage", precision = 10, scale = 2)
    private BigDecimal dailyWage;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "job_type", nullable = false, length = 50)
    private String jobType;

    @Column(name = "machine_id", length = 50)
    private String machineId;

    @Column(name = "created_at", columnDefinition = "timestamp default now()")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", columnDefinition = "timestamp default now()")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Optional: automatically update `updatedAt` before saving
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
