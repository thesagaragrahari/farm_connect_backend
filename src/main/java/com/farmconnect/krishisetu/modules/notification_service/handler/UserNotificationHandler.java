package com.farmconnect.krishisetu.modules.notification_service.handler;

import com.farmconnect.krishisetu.common.event.user.UserEvent;
import com.farmconnect.krishisetu.modules.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserNotificationHandler {

    private final EmailService emailService;

    @Value("${app.frontend.base-url}")
    private String FRONTEND_BASE_URL;

    @Value("${app.backend.base-url}")
    private String BACKEND_BASE_URL;
    

    @Async
    public void handle(UserEvent event) {

         log.info("⚡ Running in thread: {}", Thread.currentThread().getName());

        Context context = new Context();
        context.setVariable("name", event.getName());
        context.setVariable("role", event.getRole());
        context.setVariable("event", event.getEventType().name());
        context.setVariable("baseUrl", BACKEND_BASE_URL);
        
        String subject;
        String template = "email-content";

        switch (event.getEventType()) {

            case EMAIL_VERIFICATION:
            case RESEND_VERIFICATION:
                subject = "Verify your KrishiSetu Account";
                context.setVariable("link", BACKEND_BASE_URL + "/verify?token=" + event.getToken());
                break;

            case REGISTERED_AND_VERIFIED:
                subject = "Welcome to KrishiSetu!";
                break;

            case FORGOT_PASSWORD:
                subject = "Reset your Password";
                context.setVariable("link", BACKEND_BASE_URL + "/reset?token=" + event.getToken());
                break;

            case ACCOUNT_LOCKED:
                subject = "Security Alert: Account Locked";
                context.setVariable("link", BACKEND_BASE_URL + "/unlock");
                break;

            case PASSWORD_CHANGED:
                subject = "Password Changed Successfully";
                break;

            case PROFILE_COMPLETED:
                subject = "Profile Completed";
                break;

            case WELCOME_MESSAGE:
                subject = "Welcome to KrishiSetu!";
                break;

            default:
                subject = "Account Notification";
                break;
        }

        emailService.sendHtmlEmail(
                event.getEmail(),
                subject,
                template,
                context
        );
        
        log.info("Email is processed for event: {}", event.getEventType().name());
    }
}