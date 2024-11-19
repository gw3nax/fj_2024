package ru.gw3nax.lesson15.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.gw3nax.lesson15.dto.PayloadDto;
import ru.gw3nax.lesson15.service.TextReciever;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    private final TextReciever textReciever;


    @RetryableTopic(
            attempts = "1",
            kafkaTemplate = "kafkaTemplate",
            dltTopicSuffix = "_dlq",
            include = RuntimeException.class
    )

    @KafkaListener(topics = "responses", groupId = "listen", containerFactory = "kafkaListener")
    public void listen(@Payload PayloadDto payloadDto, Acknowledgment acknowledgment) {
        textReciever.storeText(payloadDto);
        acknowledgment.acknowledge();
    }
}