package com.farmconnect.krishisetu.users_management.model;

import java.time.LocalDateTime;

import org.locationtech.jts.geom.Point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    private Integer userId;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Point location;
    private String password;
}
