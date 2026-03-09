package com.farmconnect.krishisetu.modules.notification_service.listener;


import com.farmconnect.krishisetu.modules.auth_service.event.UserRegisteredEvent;
import com.farmconnect.krishisetu.modules.notification_service.service.EmailService;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisteredListener {

    private final EmailService emailService;

    @EventListener
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {

        emailService.sendWelcomeEmail(
                event.getEmail(),
                event.getName()
        );
    }
}