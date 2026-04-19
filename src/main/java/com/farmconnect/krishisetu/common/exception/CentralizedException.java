package com.farmconnect.krishisetu.common.exception;



public class CentralizedException extends RuntimeException {
    private final ErrorDefinition errorDefinition;
    private final Object[] args;

    public CentralizedException(ErrorDefinition errorDefinition, Object... args) {
        super(String.format(errorDefinition.getMessage(), args));
        this.errorDefinition = errorDefinition;
        this.args = args;
    }

    public ErrorDefinition getErrorDefinition() { return errorDefinition; }
}