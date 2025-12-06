package com.farmconnect.krishisetu.users_management.enums;

public enum UserStatus {
    AVAILABLE,
    BUSY,
    INACTIVE;

    public static boolean isValid(String status) {
    for (UserStatus s : UserStatus.values()) {
        if (s.toString().equalsIgnoreCase(status)) {
            return true;
        }
    }
    return false;
    }
}
