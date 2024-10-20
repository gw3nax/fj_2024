package ru.gw3nax.kudagoapi.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.gw3nax.kudagoapi.client.dto.KudaGoEventResponse;
import ru.gw3nax.kudagoapi.client.dto.KudaGoEventsResponseWrapper;
import ru.gw3nax.kudagoapi.configuration.ApplicationConfig;
import ru.gw3nax.kudagoapi.exception.ServiceException;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class EventsClient {
    private final HttpClient client = HttpClientBuilder.create().disableCookieManagement().build();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String eventsBaseUrl;

    @Autowired
    EventsClient(ApplicationConfig applicationConfig) {
        eventsBaseUrl = applicationConfig.eventsBaseUrl();
    }

    public List<KudaGoEventResponse> getAllEntities(Long actualSince, Long actualUntil) {
        log.info("Events client is invoked.");
        try {
            log.info("Actual Since: " + actualSince);
            log.info("Actual Until: " + actualUntil);
            String url = eventsBaseUrl + "?actual_since=" + actualSince + "&actual_until=" + actualUntil;
            String response = EntityUtils.toString(client.execute(new HttpGet(url)).getEntity());
            log.debug("Events response: {}", response);
            KudaGoEventsResponseWrapper wrapper = mapper.readValue(response, new TypeReference<KudaGoEventsResponseWrapper>() {});
            return wrapper.getResults();
        } catch (IOException e) {
            throw new ServiceException("Failed to fetch events", 500);
        }
    }
}
