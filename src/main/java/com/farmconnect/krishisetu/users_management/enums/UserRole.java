package com.farmconnect.krishisetu.users_management.enums;

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
