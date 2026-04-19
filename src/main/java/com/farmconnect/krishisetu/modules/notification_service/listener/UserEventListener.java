package com.farmconnect.krishisetu.modules.notification_service.listener;

import com.farmconnect.krishisetu.common.event.user.UserEvent;
import com.farmconnect.krishisetu.common.kafka.KafkaTopics;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserEventListener {

    @PostConstruct
    public void init() {
        System.out.println("🔥 Listener initialized");
    }

    @KafkaListener(
        topics = "user.events",
        containerFactory = "kafkaListenerContainerFactory",
        autoStartup = "true"
    )
    public void consume(String event) {
        System.out.println("🔥 RAW EVENT: " + event);
    }
}