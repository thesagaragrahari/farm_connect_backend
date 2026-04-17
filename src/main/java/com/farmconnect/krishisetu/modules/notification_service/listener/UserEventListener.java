package com.farmconnect.krishisetu.modules.notification_service.listener;

import com.farmconnect.krishisetu.common.event.user.UserEvent;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserEventListener {

    @KafkaListener(
        topics = "user.events",
        groupId = "notification-group-v2",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(UserEvent event) {
        System.out.println("🔥 RECEIVED USER EVENT: " + event);
    }
}