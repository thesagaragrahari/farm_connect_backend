package com.farmconnect.krishisetu.security.facade;


public record SecurityUserDto(
        Long userId,
        String email,
        String password,
        String role,
        boolean emailVerified,
        boolean accountLocked,
        boolean profileCompleted
) {}