package ru.gw3nax.lesson15;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.gw3nax.lesson15.configuration.kafka.KafkaConsumerProperties;
import ru.gw3nax.lesson15.configuration.kafka.KafkaProducerProperties;

@SpringBootApplication
@EnableConfigurationProperties({KafkaConsumerProperties.class, KafkaProducerProperties.class})
public class Lesson15Application {

    public static void main(String[] args) {
        SpringApplication.run(Lesson15Application.class, args);
    }

}
