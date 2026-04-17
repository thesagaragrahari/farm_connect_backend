package com.farmconnect.krishisetu.common.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.farmconnect.krishisetu.common.event.base.BaseEvent;
import com.farmconnect.krishisetu.common.event.user.UserEvent;



@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(String topic, UserEvent event) {

        log.info("📤 Sending event to Kafka topic: {} payload: {}", topic, event);

        kafkaTemplate.send(topic, event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("✅ Event sent -> topic: {}, partition: {}, offset: {}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    } else {
                        log.error("❌ Failed to send event", ex);
                    }
                });
    }
}
