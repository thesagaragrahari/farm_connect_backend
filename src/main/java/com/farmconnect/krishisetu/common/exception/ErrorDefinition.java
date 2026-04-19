package com.farmconnect.krishisetu.common.exception;

public interface ErrorDefinition {
    String getCode();
    String getMessage();
    int getStatus();
}