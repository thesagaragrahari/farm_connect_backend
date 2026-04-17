package com.farmconnect.krishisetu.modules.user_service.enums;

public enum UserRole {
    FARMER("farmer"),
    WORKER("worker"),
    OPERATOR("operator"),
    ADMIN("admin");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public static boolean isValid(String role) {
    for (UserRole r : UserRole.values()) {
        if (r.toString().equalsIgnoreCase(role)) {
            return true;
        }
    }
    return false;
    }
}
