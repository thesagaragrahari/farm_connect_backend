package com.farmconnect.krishisetu.job_management.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import org.locationtech.jts.geom.Point;



@Entity
@Table(name = "job_location", schema = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobLocation {

    @Id
    @GeneratedValue(generator = "UUID")
    @org.hibernate.annotations.GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "job_location_id", updatable = false, nullable = false)
    private UUID jobLocationId;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    // PostGIS geography(Point,4326)
    @Column(name = "location", columnDefinition = "geography(Point,4326)")
    private Point location;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "pincode", length = 20)
    private String pincode;
}
