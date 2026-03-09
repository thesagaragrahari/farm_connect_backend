package com.farmconnect.krishisetu.modules.notification_service.producer;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.farmconnect.krishisetu.modules.notification_service.event.NotificationEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationEventProducer {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    private static final String TOPIC = "notification-topic";

    public void publish(NotificationEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}