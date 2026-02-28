package com.farmconnect.krishisetu.CommonUtility.Services;



import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.farmconnect.krishisetu.CommonUtility.Models.ResetPasswordEmailEvent;

@Service
public class KafkaResetPasswordEmailProducer {

    private final KafkaTemplate<String, ResetPasswordEmailEvent> kafkaTemplate;

    public KafkaResetPasswordEmailProducer(
            KafkaTemplate<String, ResetPasswordEmailEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendResetEmail(String email, String resetLink) {

        ResetPasswordEmailEvent event = new ResetPasswordEmailEvent();
        event.setEmail(email);
        event.setResetLink(resetLink);

        kafkaTemplate.send("reset-password-email-topic", event);
    }

    public void sendVerificationEmail(String email, String resetLink) {

        ResetPasswordEmailEvent event = new ResetPasswordEmailEvent();
        event.setEmail(email);
        event.setResetLink(resetLink);

        kafkaTemplate.send("verify-email-topic", event);
    }

    
}
