package com.farmconnect.krishisetu.users_management.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "worker_skill_mapping", schema = "users",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"worker_id", "skill_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkerSkillMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "worker_id", nullable = false)
    private Integer workerId;

    @Column(name = "skill_id", nullable = false)
    private Integer skillId;

    
    @ManyToOne
    @JoinColumn(name = "worker_id", insertable = false, updatable = false)
    private Worker worker;

    @ManyToOne
    @JoinColumn(name = "skill_id", insertable = false, updatable = false)
    private Skill skill;
    
}
