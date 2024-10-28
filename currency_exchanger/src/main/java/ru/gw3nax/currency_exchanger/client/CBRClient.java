package ru.gw3nax.currency_exchanger.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import ru.gw3nax.currency_exchanger.client.dto.CBRResponse;
import ru.gw3nax.currency_exchanger.config.ApplicationConfig;
import ru.gw3nax.currency_exchanger.exceptions.CBRUnavailableException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class CBRClient {

    private final String BASEURL;
    private final XmlMapper xmlMapper = new XmlMapper();
    private HttpClient httpClient = HttpClientBuilder.create().build();

    @Autowired
    CBRClient(ApplicationConfig applicationConfig) {
        this.BASEURL = applicationConfig.cbrBaseUrl();
    }

    @Cacheable(value = "cbr_cache", key = "#root.method.name")
    @CircuitBreaker(name = "cbrService", fallbackMethod = "getCurrenciesFallback")
    public List<CBRResponse> getCurrencies() {
        log.info("Getting data from CBR...");
        try {
            String entity = EntityUtils.toString(httpClient.execute(new HttpGet(BASEURL)).getEntity());
            log.info("Received response: {}", entity);
            return xmlMapper.readValue(entity, new TypeReference<>() {});
        } catch (IOException e) {
            throw new CBRUnavailableException("CBR service is unavailable");
        }
    }

    public void getCurrenciesFallback(CBRUnavailableException throwable) {
        log.info("CBR service is unavailable.");
        throw new CBRUnavailableException("CBR service is unavailable.");
    }
}
