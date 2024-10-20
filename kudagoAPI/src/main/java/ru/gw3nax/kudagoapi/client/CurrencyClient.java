package ru.gw3nax.kudagoapi.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.gw3nax.kudagoapi.client.dto.CurrencyRequest;
import ru.gw3nax.kudagoapi.client.dto.CurrencyResponce;
import ru.gw3nax.kudagoapi.configuration.ApplicationConfig;
import ru.gw3nax.kudagoapi.exception.ServiceException;

@Component
@Slf4j
public class CurrencyClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String currencyBaseUrl;

    @Autowired
    CurrencyClient(ApplicationConfig applicationConfig) {
        currencyBaseUrl = applicationConfig.currencyBaseUrl();
    }

    public Double postForConvertCurrency(CurrencyRequest currencyRequest) {
        log.info("Currency client is invoked.");
        try {
            CurrencyResponce currencyResponce = restTemplate.postForObject(currencyBaseUrl, currencyRequest, CurrencyResponce.class);
            log.info("Currency response {}", currencyResponce);
            return currencyResponce.getConvertedAmount();
        } catch (Exception e) {
            log.error("Currency response error", e);
            throw new ServiceException("Failed to get events", 500);
        }

    }
}
