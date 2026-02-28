package com.farmconnect.krishisetu.CommonUtility.Services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.farmconnect.krishisetu.CommonUtility.Models.ResetPasswordEmailEvent;
import com.farmconnect.krishisetu.CommonUtility.Models.VerificationEmailEvent;




@Service
public class EmailEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EmailEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendVerificationEmail(String email, String verificationLink) {

        VerificationEmailEvent event = new VerificationEmailEvent();
        event.setEmail(email);
        event.setVerificationLink(verificationLink);

        kafkaTemplate.send("verify-email-topic", event);
    }

    public void sendResetEmail(String email, String resetLink) {

        ResetPasswordEmailEvent event = new ResetPasswordEmailEvent();
        event.setEmail(email);
        event.setResetLink(resetLink);

        kafkaTemplate.send("reset-password-email-topic", event);
    }
}