package com.farmconnect.krishisetu.common.exception;


public record ApiResponse<T>(
    boolean success,    // Always true for this class
    String message,    // A friendly success message
    T data,            // The actual payload
    long timestamp
) {
    // Factory method for a clean syntax
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data, System.currentTimeMillis());
    }
}