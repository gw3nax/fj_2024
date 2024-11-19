package ru.gw3nax.lesson15.service;

import jakarta.validation.Payload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.gw3nax.lesson15.configuration.kafka.KafkaProducerProperties;
import ru.gw3nax.lesson15.dto.PayloadDto;

@Service
@Slf4j
public class TextSender {
    private final KafkaProducerProperties kafkaProperties;
    private final KafkaTemplate<String, PayloadDto> kafkaTemplate;

    public TextSender(
            KafkaProducerProperties kafkaProperties,
            KafkaTemplate<String, PayloadDto> kafkaTemplate
    ) {
        this.kafkaProperties = kafkaProperties;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMesage(PayloadDto payloadDto) {
        kafkaTemplate.send(kafkaProperties.topicProp().name(), payloadDto);
    }
}
