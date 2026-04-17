package com.farmconnect.krishisetu.modules.auth_service.DTOs;

import lombok.Data;

@Data
public class LoginReq {
    
    String email;
    String password;
    String role;
}
