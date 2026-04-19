package com.farmconnect.krishisetu.modules.auth_service.utility;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.farmconnect.krishisetu.common.event.base.BaseEvent;
import com.farmconnect.krishisetu.common.event.user.UserEvent;
import com.farmconnect.krishisetu.modules.auth_service.entities.UserActionToken;
import com.farmconnect.krishisetu.modules.auth_service.entities.UserSecurityState;

import com.farmconnect.krishisetu.modules.auth_service.models.TokenType;
import com.farmconnect.krishisetu.modules.auth_service.repositories.UserActionTokenRepository;
import com.farmconnect.krishisetu.modules.auth_service.repositories.UserSecurityStateRepository;
import com.farmconnect.krishisetu.modules.notification_service.handler.UserNotificationHandler;
import com.farmconnect.krishisetu.modules.user_service.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthServiceUtil {

    private final Logger logger = LoggerFactory.getLogger(AuthServiceUtil.class.getName());

    private final UserActionTokenRepository actionTokenRepo;
    private final UserSecurityStateRepository securityStateRepo;
    private final TokenUtil tokenUtil;
    private final UserActionTokenRepository tokenRepo;
    private final UserNotificationHandler handler;


    @Value("${app.backend.base-url:http://localhost:8080}")
    private String backendBaseUrl;
    @Value("${app.frontend.reset-password-url:http://localhost:3000/reset-password}")
    private String resetPasswordBaseUrl;
    
    /* =========================================================
                    INTERNAL HELPERS
       ========================================================= */

    public void initSecurity(Long userId) {
        UserSecurityState state = new UserSecurityState();
        state.setUserId(userId);
        state.setEmailVerified(false);
        state.setAccountLocked(false);
        state.setProfileCompleted(false);
        state.setTokenVersion(0);
        securityStateRepo.save(state);
    }


    public void validateToken(UserActionToken token) {

        if (token.isUsed())
            throw new RuntimeException("Token already used");

        if (token.getExpiryTime()
                .isBefore(LocalDateTime.now()))
            throw new RuntimeException("Token expired");
    }

    void verifyEmail(User user, TokenType type) {
        String token = UUID.randomUUID().toString();
        UserActionToken actionToken = new UserActionToken();
        actionToken.setUserId(user.getUserId());
        actionToken.setTokenHash(token);
        actionToken.setTokenType(type);
        actionToken.setExpiryTime(LocalDateTime.now().plusHours(24));
        actionTokenRepo.save(actionToken);
        return;
    }


    public String createActionToken(Long userId, TokenType type) {
        String raw = tokenUtil.generateRawToken();
        UserActionToken token = new UserActionToken();
        token.setUserId(userId);
        token.setTokenHash(tokenUtil.hashToken(raw));
        token.setTokenType(type);
        token.setUsed(false);
        token.setExpiryTime(LocalDateTime.now().plusMinutes(15));
        tokenRepo.save(token);
        logger.info("Generated {} token for {}",type,userId );
        return raw;
    }


    public void callEmailHandler(UserEvent event) {
        try{
            handler.handle(event);
        }catch(Exception e){
            logger.error("Error while sending email notification for event: {}", event, e);
        }
    }

    

    // private void publishActionTokenNotification(User user, TokenType type, String rawToken) {
    //     NotificationEvent event = new NotificationEvent();
    //     event.setRecipient(user.getEmail());

    //     switch (type) {
    //         case EMAIL_VERIFICATION -> {
    //             event.setType(NotificationType.EMAIL_VERIFICATION);
    //             event.setMetadata(
    //                     Map.of(
    //                             "verificationLink",
    //                             backendBaseUrl + "/api/auth/verify-email?token=" + rawToken
    //                     )
    //             );
    //         }
    //         case PASSWORD_RESET -> {
    //             event.setType(NotificationType.PASSWORD_RESET);
    //             event.setMetadata(
    //                     Map.of(
    //                             "resetLink",
    //                             resetPasswordBaseUrl + "?token=" + rawToken
    //                     )
    //             );
    //         }
    //         default -> {
    //             logger.warn("No notification mapping for token type {}", type);
    //             return;
    //         }
    //     }

    //     notificationEventProducer.publish(event);
    // }

}
