package com.farmconnect.krishisetu.job_management.model;

import java.util.UUID;

import org.locationtech.jts.geom.Point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobLocationProfile {
    private UUID jobLocationId;
    private UUID jobId;
    private Point location;
    private String address;
    private String city;
    private String state;
    private String pincode;
}
