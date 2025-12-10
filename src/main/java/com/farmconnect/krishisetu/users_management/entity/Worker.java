package com.farmconnect.krishisetu.users_management.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Column(name = "daily_wage", precision = 10, scale = 2)
    private BigDecimal dailyWage;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "job_type", nullable = false, length = 50)
    private String jobType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id", referencedColumnName = "machine_id", nullable = true)
    private Machine machine;

    // One worker → many mappings
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkerSkillMapping> workerSkillsMappings;

    // @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<Skill> workerSkills;

}
