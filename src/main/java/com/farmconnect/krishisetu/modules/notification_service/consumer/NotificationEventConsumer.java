package com.farmconnect.krishisetu.modules.notification_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.farmconnect.krishisetu.modules.notification_service.event.NotificationEvent;
import com.farmconnect.krishisetu.modules.notification_service.models.NotificationType;
import com.farmconnect.krishisetu.modules.notification_service.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final EmailService emailService;

    @KafkaListener(
            topics = "email-topic",
            groupId = "notification-group"
    )
    public void consume(NotificationEvent event) {

        NotificationType type = event.getType();

        switch (type) {

            case EMAIL_VERIFICATION -> {
                String link = event.getMetadata().get("verificationLink");
                emailService.sendVerificationEmail(event.getRecipient(), link);
            }

            case PASSWORD_RESET -> {
                String link = event.getMetadata().get("resetLink");
                emailService.sendResetPasswordMail(event.getRecipient(), link);
            }

            case WELCOME_EMAIL -> {
                String name = event.getMetadata().get("name");
                emailService.sendWelcomeEmail(event.getRecipient(), name);
            }

            case PASSWORD_CHANGED -> {
                emailService.sendPasswordChangedAlert(event.getRecipient());
            }

            default -> throw new IllegalArgumentException(
                    "Unsupported notification type: " + type
            );
        }
    }
}