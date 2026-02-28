package com.farmconnect.krishisetu.users_management.model;

import java.time.LocalDateTime;

import com.farmconnect.krishisetu.CommonUtility.Models.PointDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    //private Integer userId;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PointDTO location;
    private String password;
}
