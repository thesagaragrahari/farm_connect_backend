package com.farmconnect.krishisetu.modules.user_service.model;

import java.time.LocalDateTime;

import com.farmconnect.krishisetu.modules.user_service.enums.UserRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    // @NotBlank
    // @Pattern(regexp = "farmer|worker|operator|admin", message = "Role should be valid (farmer, worker, operator, admin)")
    private String role;
    //private LocalDateTime createdAt;
    //private LocalDateTime updatedAt;
    private PointDTO location;
    //private String password;
}
