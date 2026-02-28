package com.farmconnect.krishisetu.CommonUtility.Services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.farmconnect.krishisetu.CommonUtility.Entities.ForgotPasswordAttempt;
import com.farmconnect.krishisetu.CommonUtility.Repos.ForgotPasswordAttemptRepository;
import static com.farmconnect.krishisetu.CommonUtility.CommonConstants.UserSpecificConstants.MAX_ATTEMPTS;
import static com.farmconnect.krishisetu.CommonUtility.CommonConstants.UserSpecificConstants.WINDOW_MINUTES;

@Service
public class ForgotPasswordRateLimiter {

    private final ForgotPasswordAttemptRepository repository;

    public ForgotPasswordRateLimiter(
            ForgotPasswordAttemptRepository repository) {
        this.repository = repository;
    }

    public void validate(String identifier) {

        ForgotPasswordAttempt attempt =
            repository.findByIdentifier(identifier)
                .orElseGet(() -> {
                    ForgotPasswordAttempt a = new ForgotPasswordAttempt();
                    a.setIdentifier(identifier);
                    a.setAttemptCount(0);
                    return a;
                });

        if (attempt.getLastAttemptTime() != null &&
            attempt.getLastAttemptTime()
                .isAfter(LocalDateTime.now().minusMinutes(WINDOW_MINUTES))) {

            if (attempt.getAttemptCount() >= MAX_ATTEMPTS) {
                throw new RuntimeException(
                    "Too many forgot-password attempts. Try later."
                );
            }
        } else {
            attempt.setAttemptCount(0);
        }

        attempt.setAttemptCount(attempt.getAttemptCount() + 1);
        attempt.setLastAttemptTime(LocalDateTime.now());

        repository.save(attempt);
    }
}
