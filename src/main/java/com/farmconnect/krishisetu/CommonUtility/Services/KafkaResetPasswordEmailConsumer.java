package com.farmconnect.krishisetu.CommonUtility.Services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.farmconnect.krishisetu.CommonUtility.Models.ResetPasswordEmailEvent;

@Service
public class KafkaResetPasswordEmailConsumer {

    private final EmailService emailService;

    public KafkaResetPasswordEmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(
        topics = "reset-password-email-topic",
        groupId = "reset-password-email-group"
    )
    public void consume(ResetPasswordEmailEvent event) {

        emailService.sendResetPasswordMail(
            event.getEmail(),
            event.getResetLink()
        );
    }
}
