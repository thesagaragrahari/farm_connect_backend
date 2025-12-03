package com.farmconnect.krishisetu.users_management.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "farmers", schema = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Farmer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "farmer_id", nullable = false)
    private Integer farmerId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "land_area_acre", precision = 10, scale = 2)
    private BigDecimal landAreaAcre;

    @Column(name = "created_at", columnDefinition = "timestamp default now()")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", columnDefinition = "timestamp default now()")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Automatically update updatedAt timestamp
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
