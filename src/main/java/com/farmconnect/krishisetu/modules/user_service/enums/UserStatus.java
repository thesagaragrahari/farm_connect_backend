package com.farmconnect.krishisetu.modules.user_service.enums;

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
