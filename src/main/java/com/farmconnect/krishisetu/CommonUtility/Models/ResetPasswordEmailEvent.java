package com.farmconnect.krishisetu.CommonUtility.Models;

import java.io.Serializable;
import lombok.Data;

@Data
public class ResetPasswordEmailEvent implements Serializable {
    private String email;
    private String resetLink;
}
