package com.farmconnect.krishisetu.modules.auth_service.models;

import java.io.Serializable;
import lombok.Data;

@Data
public class ResetPasswordEmailEvent implements Serializable {
    private String email;
    private String resetLink;
}
