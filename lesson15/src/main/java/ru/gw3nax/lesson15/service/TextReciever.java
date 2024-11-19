package ru.gw3nax.lesson15.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gw3nax.lesson15.dto.PayloadDto;

@Service
@Slf4j
public class TextReciever {

    private final TextSender textSender;

    public TextReciever(TextSender textSender) {
        this.textSender = textSender;
    }

    public void storeText(PayloadDto payloadDto) {
        log.info("Message stored: " + payloadDto.getMessage());
    }
}
