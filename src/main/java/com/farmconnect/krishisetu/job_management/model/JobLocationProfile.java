package com.farmconnect.krishisetu.job_management.model;


import org.locationtech.jts.geom.Point;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobLocationProfile {

    private UUID jobLocationId;

    // --- Spatial Data Representation ---
    // In a DTO, you typically send simplified coordinates 
    // instead of the complex spatial object itself.
    private Point location;
    
    // Placeholder: If you send the raw spatial string/WKT
    private String locationWkt; 
    
    // --- Address Fields ---
    private String address;
    private String city;
    private String state;
    private String pincode;
}