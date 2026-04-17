package com.farmconnect.krishisetu.common.event.user;

public enum UserEventType {
    REGISTERED,//register event, sent after user is created but before email verification
    REGISTERED_AND_VERIFIED, //sent after user is created and email is verified, used to send welcome email and other post verification actions
    
    EMAIL_VERIFICATION, //sent when user needs to verify email, used to send verification email
    EMAIL_VERIFIED,// successful email verified
    RESEND_VERIFICATION,// resend verification email


    FORGOT_PASSWORD, // forgot password requested, used to send password reset email
    PASSWORD_RESET, // token for password reset

    PASSWORD_CHANGED, // password successfully changed, used to send notification email

    ACCOUNT_LOCKED, // warning email for account locked
    ACCOUNT_UNLOCKED,// account unlock request
    PROFILE_COMPLETED,// profile completed
    WELCOME_MESSAGE 
}