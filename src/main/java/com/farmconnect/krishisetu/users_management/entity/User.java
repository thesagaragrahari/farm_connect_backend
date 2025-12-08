package com.farmconnect.krishisetu.users_management.entity;

import org.locationtech.jts.geom.Point;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users", schema = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "email", nullable = false, length = 120, unique = true)
    private String email;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "role", nullable = false, length = 20)
    private String role;

    @Builder.Default
    @Column(name = "created_at", columnDefinition = "timestamp default now()")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default    
    @Column(name = "updated_at", columnDefinition = "timestamp default now()")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Automatically update updatedAt timestamp
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Column(name = "location", columnDefinition = "GEOGRAPHY(Point, 4326)")
    private Point location;

    @Column(name = "password")
    private String password;

    public User orElseThrow(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
    }

}

// for setting location point
// GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);

// Point point = factory.createPoint(new Coordinate(longitude, latitude));
// user.setLocation(point);
