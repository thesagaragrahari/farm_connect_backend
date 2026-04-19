package com.farmconnect.krishisetu.common.exception;

public record ApiErrorResponse(
    String code,
    String message,
    long timestamp,
    String traceId
) {}