package com.farmconnect.krishisetu.users_management.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "machines", schema = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "machine_id", nullable = false)
    private Integer machineId;

    @Column(name = "machine_name", nullable = false, length = 100)
    private String machineName;

    @Column(name = "machine_url", columnDefinition = "TEXT")
    private String machineUrl;

    @Column(name = "machine_doc_id", length = 100)
    private String machineDocId;

    @Column(name = "skill_id", nullable = false)
    private Integer skillId;

    // Optional: JPA relationship to Skill
    /*
    @ManyToOne
    @JoinColumn(name = "skill_id", insertable = false, updatable = false)
    private Skill skill;
    */
}
