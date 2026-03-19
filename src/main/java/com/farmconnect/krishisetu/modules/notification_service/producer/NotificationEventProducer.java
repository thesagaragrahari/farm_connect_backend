package com.farmconnect.krishisetu.modules.notification_service.producer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.farmconnect.krishisetu.modules.notification_service.event.NotificationEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationEventProducer {

    Logger logger = LoggerFactory.getLogger(NotificationEventProducer.class);
    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    private static final String TOPIC = "email-topic";

    public void publish(NotificationEvent event) {
        kafkaTemplate.send(TOPIC, event);
        logger.info("Published notification event: {}", event);
    }
}