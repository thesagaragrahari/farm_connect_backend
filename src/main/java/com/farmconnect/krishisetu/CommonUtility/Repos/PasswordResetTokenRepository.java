package com.farmconnect.krishisetu.CommonUtility.Repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmconnect.krishisetu.CommonUtility.Entities.PasswordResetToken;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByTokenHashAndUsedFalse(String tokenHash);
}
