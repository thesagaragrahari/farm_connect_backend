package com.farmconnect.krishisetu.common.exception;



public enum AppError implements ErrorDefinition {
    // Auth Module
    EMAIL_ALREADY_EXISTS("EMAIL-001", "The email %s is already exists. Please login or try with another email", 409),

    EMAIL_NOT_VERIFIED("EMAIL-002", "The email %s has not been verified yet.Please verify, a verification mail sent", 403),


    USER_ACCOUNT_LOCKED("USER-001", "Account Locked. Please connect with Admin", 401),

    AUTH_INVALID_CREDENTIALS("AUTH-001", "Invalid credentials.Try again", 401),

    AUTH_EXPIRED("A001", "Session expired. Please login again.", 401),
    AUTH_DENIED("A002", "Insufficient permissions.", 403),

    // User Module
    USER_NOT_FOUND("U001", "User with ID %s not found.", 404),
    
    // System
    INTERNAL_ERROR("S500", "A system error occurred.", 500);

    private final String code;
    private final String message;
    private final int status;

    AppError(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override public String getCode() { return code; }
    @Override public String getMessage() { return message; }
    @Override public int getStatus() { return status; }

}