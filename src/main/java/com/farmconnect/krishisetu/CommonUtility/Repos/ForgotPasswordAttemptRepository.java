package com.farmconnect.krishisetu.CommonUtility.Repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmconnect.krishisetu.CommonUtility.Entities.ForgotPasswordAttempt;

public interface ForgotPasswordAttemptRepository
        extends JpaRepository<ForgotPasswordAttempt, Long> {

    Optional<ForgotPasswordAttempt> findByIdentifier(String identifier);
}
