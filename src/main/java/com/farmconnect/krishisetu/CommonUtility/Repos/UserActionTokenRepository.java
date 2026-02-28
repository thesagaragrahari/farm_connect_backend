package com.farmconnect.krishisetu.CommonUtility.Repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmconnect.krishisetu.CommonUtility.Entities.UserActionToken;
import com.farmconnect.krishisetu.users_management.enums.TokenType;

public interface UserActionTokenRepository
        extends JpaRepository<UserActionToken, Long> {

    Optional<UserActionToken> findByTokenHashAndTokenType(
            String tokenHash,
            TokenType tokenType
    );
}
