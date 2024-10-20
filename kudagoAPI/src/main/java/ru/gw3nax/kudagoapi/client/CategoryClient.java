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
import ru.gw3nax.kudagoapi.client.dto.KudaGoCategoryResponse;
import ru.gw3nax.kudagoapi.configuration.ApplicationConfig;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class CategoryClient implements Client<KudaGoCategoryResponse> {
    private final HttpClient client = HttpClientBuilder.create().build();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String categoryBaseUrl;

    @Autowired
    CategoryClient(ApplicationConfig applicationConfig) {
        categoryBaseUrl = applicationConfig.categoryBaseUrl();
    }

    @Override
    public List<KudaGoCategoryResponse> getAllEntities() {
        try {
            String response = EntityUtils.toString(client.execute(new HttpGet(categoryBaseUrl)).getEntity());
            log.debug("Category response {}", response);
            return mapper.readValue(response, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
