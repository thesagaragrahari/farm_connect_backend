package com.farmconnect.krishisetu.config;

import com.farmconnect.krishisetu.common.event.base.BaseEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

        @Bean
        public ConsumerFactory<String, String> consumerFactory() {
                return new DefaultKafkaConsumerFactory<>(
                        Map.of(
                                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                                ConsumerConfig.GROUP_ID_CONFIG, "debug-group",
                                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class
                        )
                );
        }

//     private Map<String, Object> consumerConfigs() {
//         Map<String, Object> config = new HashMap<>();

//         config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//         config.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-group-v2");
//         config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

//         return config;
//     }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {

                ConcurrentKafkaListenerContainerFactory<String, String> factory =
                        new ConcurrentKafkaListenerContainerFactory<>();
                factory.setConsumerFactory(consumerFactory());
                factory.setConcurrency(2); // optional

                return factory;
        }
}