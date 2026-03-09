package com.farmconnect.krishisetu.modules.auth_service.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;

import com.farmconnect.krishisetu.modules.auth_service.entities.UserActionToken;
import com.farmconnect.krishisetu.modules.auth_service.event.ActionTokenCreatedEvent;
import com.farmconnect.krishisetu.modules.auth_service.event.UserRegisteredEvent;
import com.farmconnect.krishisetu.modules.auth_service.models.TokenType;
import com.farmconnect.krishisetu.modules.auth_service.repositories.UserActionTokenRepository;
import com.farmconnect.krishisetu.modules.user_service.entity.User;


public class EventPublisherService {

    private UserActionTokenRepository actionTokenRepo;
    private ApplicationEventPublisher eventPublisher;

    void verifyEmailEvent(User user, TokenType type) {
    String token = UUID.randomUUID().toString();
    UserActionToken actionToken = new UserActionToken();
    actionToken.setUser(user);
    actionToken.setTokenHash(token);
    actionToken.setTokenType(type);
    actionToken.setExpiryTime(LocalDateTime.now().plusHours(24));
    actionTokenRepo.save(actionToken);
    eventPublisher.publishEvent(
            new ActionTokenCreatedEvent(
                    user.getEmail(),
                    token,
                    TokenType.EMAIL_VERIFICATION
            )
    );
    // welcome mail after registration
    eventPublisher.publishEvent(
            new UserRegisteredEvent(
                    user.getUserId(),
                    user.getEmail(),
                    user.getFullName()
            )
    );
    return;
    }


    void welcomeEvent(User user, TokenType type) {

    String token = UUID.randomUUID().toString();

    UserActionToken actionToken = new UserActionToken();
    actionToken.setUser(user);
    actionToken.setTokenHash(token);
    actionToken.setTokenType(type);
    actionToken.setExpiryTime(LocalDateTime.now().plusHours(24));

    actionTokenRepo.save(actionToken);
    
    //verification email after registration
    eventPublisher.publishEvent(
            new ActionTokenCreatedEvent(
                    user.getEmail(),
                    token,
                    TokenType.EMAIL_VERIFICATION
            )
    );
    // welcome mail after registration
    eventPublisher.publishEvent(
            new UserRegisteredEvent(
                    user.getUserId(),
                    user.getEmail(),
                    user.getFullName()
            )
    );
    return;
    }


    
}
