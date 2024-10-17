package ru.gw3nax.kudagoapi.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ru.gw3nax.kudagoapi.client.dto.KudaGoLocationResponse;
import ru.gw3nax.kudagoapi.configuration.ApplicationConfig;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class LocationClient implements Client<KudaGoLocationResponse> {

    private final HttpClient client = HttpClientBuilder.create().build();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String locationBaseUrl;

    @Autowired
    LocationClient(ApplicationConfig applicationConfig) {
        locationBaseUrl = applicationConfig.locationBaseUrl();
    }

    @Override
    public List<KudaGoLocationResponse> getAllEntities() {
        try {
            String response = EntityUtils.toString(client.execute(new HttpGet(locationBaseUrl)).getEntity());
            log.debug("Location response {}", response);
            return mapper.readValue(response, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
