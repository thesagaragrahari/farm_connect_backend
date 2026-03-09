package com.farmconnect.krishisetu.modules.auth_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmconnect.krishisetu.modules.auth_service.entities.UserActionToken;
import com.farmconnect.krishisetu.modules.auth_service.models.TokenType;

public interface UserActionTokenRepository
        extends JpaRepository<UserActionToken, Long> {

    Optional<UserActionToken> findByTokenHashAndTokenType(
            String tokenHash,
            TokenType tokenType
    );
}
