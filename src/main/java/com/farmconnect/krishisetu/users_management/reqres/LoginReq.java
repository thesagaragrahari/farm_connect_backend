package com.farmconnect.krishisetu.users_management.reqres;

import lombok.Data;

@Data
public class LoginReq {
    //String phone;
    String email;
    String password;
    String role;
}
