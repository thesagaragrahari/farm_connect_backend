package com.farmconnect.krishisetu.modules.user_service.enums;

public enum UserRole {
    FARMER,
    WORKER,
    OPERATOR,
    ADMIN;

    public static boolean isValid(String role) {
    for (UserRole r : UserRole.values()) {
        if (r.toString().equalsIgnoreCase(role)) {
            return true;
        }
    }
    return false;
    }
}
