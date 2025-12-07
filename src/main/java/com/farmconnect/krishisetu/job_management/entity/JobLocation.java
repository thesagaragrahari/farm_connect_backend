package com.farmconnect.krishisetu.job_management.entity;

import org.locationtech.jts.geom.Point;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.UUID;

// Note: If you are using Hibernate Spatial or similar, 
// you might need additional configuration or annotations 
// to properly handle the 'geography(Point,4326)' type.

@Entity
@Table(name = "job_location", schema = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Indicates UUID generation
    @Column(name = "job_location_id")
    private UUID jobLocationId;

    // --- GEOGRAPHY/Spatial Type Handling ---
    // You must use a specific JPA type or an extension (like Hibernate Spatial) 
    // to map the PostgreSQL GEOGRAPHY type. Assuming a common standard for now.
    @Column(name = "location")
    private Point location; // Placeholder: Use a specific spatial object type here (e.g., Point, Geometry)

    @Column(name = "address", columnDefinition = "text")
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "pincode", length = 20)
    private String pincode;

    // Note: No explicit getters/setters/constructors are needed due to Lombok.
}