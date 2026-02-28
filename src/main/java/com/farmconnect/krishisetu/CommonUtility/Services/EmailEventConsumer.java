package com.farmconnect.krishisetu.CommonUtility.Services;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.farmconnect.krishisetu.CommonUtility.Models.ResetPasswordEmailEvent;
import com.farmconnect.krishisetu.CommonUtility.Models.VerificationEmailEvent;


@Service
public class EmailEventConsumer {

    private final EmailService emailService;

    public EmailEventConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(
        topics = "reset-password-email-topic",
        groupId = "email-group"
    )
    public void consumeResetMail(ResetPasswordEmailEvent event) {

        emailService.sendResetPasswordMail(
                event.getEmail(),
                event.getResetLink()
        );
    }

    @KafkaListener(
        topics = "verify-email-topic",
        groupId = "email-group"
    )
    public void consumeVerificationMail(VerificationEmailEvent event) {

        emailService.sendVerificationEmail(
                event.getEmail(),
                event.getVerificationLink()
        );
    }
}