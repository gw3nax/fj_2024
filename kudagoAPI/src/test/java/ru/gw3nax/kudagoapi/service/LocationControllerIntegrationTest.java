package ru.gw3nax.kudagoapi.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;
import ru.gw3nax.kudagoAPI.controller.dto.CategoryResponse;
import ru.gw3nax.kudagoAPI.controller.dto.LocationRequest;
import ru.gw3nax.kudagoAPI.controller.dto.LocationResponse;
import ru.gw3nax.kudagoAPI.entity.Location;
import ru.gw3nax.kudagoAPI.mapper.LocationMapper;
import ru.gw3nax.kudagoAPI.repository.LocationRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class LocationControllerIntegrationTest {

    private static final String BASEURL = "/api/v1/locations";

    @Container
    private static final WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:2.35.0")
            .withMappingFromResource("wiremock/categorySetup.json")
            .withMappingFromResource("wiremock/locationSetup.json");
    private static String wireMockUrl;

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationMapper locationMapper;

    @LocalServerPort
    private int port;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        wireMockUrl = String.format("http://%s:%d",
                wiremockServer.getHost(),
                wiremockServer.getMappedPort(8080));
        registry.add("kudago.api.location-base-url", () -> wireMockUrl + "/public-api/v1.4/place-categories");
    }

    @Test
    void getLocationByIdTest() {
        Location location = new Location(1L, "slug", "name", "test", "ru");
        locationRepository.save(location);

        LocationResponse expectedResponse = locationMapper.mapToLocationResponse(location);

        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASEURL + "/{id}").build(location.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CategoryResponse.class)
                .returnResult()
                .getResponseBody();
        assertThat(response).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    void postLocationTest() {
        LocationRequest request = new LocationRequest(1L, "slug", "name", "test", "ru");

        var expectedSize = locationRepository.findAll().size();
        webClient.post()
                .uri(BASEURL)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();

        Location savedLocation = locationRepository.findById(expectedSize+1L).get();
        assertThat(savedLocation.getSlug()).isEqualTo("slug");
        assertThat(savedLocation.getName()).isEqualTo("name");
    }

    @Test
    void updateLocationTest() {
        var location = new Location(1L, "slug", "name", "test", "ru");
        locationRepository.save(location);
        var requestForUpdate = new LocationRequest();
        requestForUpdate.setName("update");
        requestForUpdate.setSlug("update");

        webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path(BASEURL + "/{id}")
                        .build(location.getId()))
                .bodyValue(requestForUpdate)
                .exchange();

        var updatedLocation = locationRepository.findById(location.getId()).get();
        assertThat(updatedLocation.getName()).isEqualTo(requestForUpdate.getName());
        assertThat(updatedLocation.getSlug()).isEqualTo(requestForUpdate.getSlug());
    }

    @Test
    void deleteLocationTest() {
        var location = new Location(1L, "slug", "name", "test", "ru");
        locationRepository.save(location);

        webClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path(BASEURL + "/{id}")
                        .build(location.getId()))
                .exchange()
                .expectStatus().isOk();

        assertThat(locationRepository.findById(location.getId())).isEmpty();
    }
    @AfterEach
    void cleanUp() {
        locationRepository.clearAll();
    }
}
