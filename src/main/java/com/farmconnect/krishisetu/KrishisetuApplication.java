package com.farmconnect.krishisetu;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;


@SpringBootApplication
@EnableKafka
public class KrishisetuApplication {

	@Bean
public CommandLineRunner debugBeans(ApplicationContext ctx) {
    return args -> {
        System.out.println("--- KAFKA BEAN CHECK ---");
        boolean exists = ctx.containsBean("userEventListener"); // use your class name in camelCase
        System.out.println("Does UserEventListener exist? " + exists);
        
        String[] factories = ctx.getBeanNamesForType(ConcurrentKafkaListenerContainerFactory.class);
        System.out.println("Kafka Factories found: " + Arrays.toString(factories));
    };
}
	public static void main(String[] args) {
		SpringApplication.run(KrishisetuApplication.class, args);
	}

}
