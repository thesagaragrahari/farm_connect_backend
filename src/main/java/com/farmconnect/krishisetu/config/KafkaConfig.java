// package com.farmconnect.krishisetu.config;

// import com.farmconnect.krishisetu.common.event.base.BaseEvent;
// import org.apache.kafka.clients.consumer.ConsumerConfig;
// import org.apache.kafka.common.serialization.StringDeserializer;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.kafka.annotation.EnableKafka;
// import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
// import org.springframework.kafka.core.*;
// import org.springframework.kafka.support.serializer.JsonDeserializer;

// import java.util.Map;
// @Configuration
// @EnableKafka
// public class KafkaConfig {

//     @Bean
//     public ConsumerFactory<String, Object> consumerFactory() {

//         JsonDeserializer<Object> deserializer = new JsonDeserializer<>();
//         deserializer.addTrustedPackages("*");

//         return new DefaultKafkaConsumerFactory<>(
//                 Map.of(
//                         ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
//                         ConsumerConfig.GROUP_ID_CONFIG, "notification-group-v2"
//                 ),
//                 new StringDeserializer(),
//                 deserializer
//         );
//     }

//     @Bean
//     public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
//             ConsumerFactory<String, Object> consumerFactory) {

//         ConcurrentKafkaListenerContainerFactory<String, Object> factory =
//                 new ConcurrentKafkaListenerContainerFactory<>();

//         factory.setConsumerFactory(consumerFactory);
//         return factory;
//     }
// }