package com.farmconnect.krishisetu.modules.auth_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmconnect.krishisetu.modules.auth_service.entities.ForgotPasswordAttempt;

public interface ForgotPasswordAttemptRepository
        extends JpaRepository<ForgotPasswordAttempt, Long> {

    Optional<ForgotPasswordAttempt> findByIdentifier(String identifier);
}
