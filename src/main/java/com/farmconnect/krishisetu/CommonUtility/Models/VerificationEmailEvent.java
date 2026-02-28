package com.farmconnect.krishisetu.CommonUtility.Models;

import java.io.Serializable;

import lombok.Data;

@Data
public class VerificationEmailEvent implements Serializable{

    private String email;
    private String verificationLink;
}