package com.farmconnect.krishisetu.modules.user_service.DTOs;

import lombok.Data;

@Data
public class LoginReq {
    //String phone;
    String email;
    String password;
    String role;
}
