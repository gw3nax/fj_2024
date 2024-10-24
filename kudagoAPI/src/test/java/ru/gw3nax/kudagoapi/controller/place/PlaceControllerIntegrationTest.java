package ru.gw3nax.kudagoapi.controller.place;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;
import ru.gw3nax.kudagoapi.PostgresSqlInitializer;
import ru.gw3nax.kudagoapi.controller.dto.ErrorResponse;
import ru.gw3nax.kudagoapi.controller.dto.PlaceRequest;
import ru.gw3nax.kudagoapi.controller.dto.PlaceResponse;
import ru.gw3nax.kudagoapi.repository.PlaceRepository;
import ru.gw3nax.kudagoapi.service.PlaceService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ContextConfiguration(initializers = PostgresSqlInitializer.class)
public class PlaceControllerIntegrationTest {

    @Container
    private static final WireMockContainer wireMockServer = new WireMockContainer("wiremock/wiremock:2.35.0");

    private static final String BASE_URL = "/api/v2/places";

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private PlaceRepository placeRepository;

    @Test
    void createPlaceTest_shouldCreatePlace_success() {
        PlaceRequest placeRequest = PlaceRequest.builder()
                .slug("test")
                .name("test")
                .language("ru")
                .timezone("UTC+8")
                .build();

        webClient.post()
                .uri(BASE_URL)
                .bodyValue(placeRequest)
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(placeService.findAll().size()).isEqualTo(1);
    }

    @Test
    void updatePlaceTest_shouldThrowServiceException_notFound() {
        PlaceRequest placeRequest = PlaceRequest.builder()
                .slug("test")
                .name("test")
                .language("ru")
                .timezone("UTC+8")
                .build();
        placeService.save(placeRequest);

        var expectedResponse = ErrorResponse.builder()
                .code(404)
                .text("Place not found")
                .build();

        PlaceRequest updateRequest = PlaceRequest.builder()
                .id(Long.MAX_VALUE)
                .slug("update")
                .name("update")
                .language("ru")
                .timezone("UTC+8")
                .build();

        var actualResponse = webClient.put()
                .uri(BASE_URL + "/place")
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    void updatePlaceTest_shouldUpdatePlace_success() {
        PlaceRequest placeRequest = PlaceRequest.builder()
                .slug("test")
                .name("test")
                .language("ru")
                .timezone("UTC+8")
                .build();
        PlaceResponse placeResponse = placeService.save(placeRequest);


        PlaceRequest updateRequest = PlaceRequest.builder()
                .id(placeResponse.getId())
                .slug("update")
                .name("update")
                .language("ru")
                .timezone("UTC+8")
                .build();

        var actualResponse = webClient.put()
                .uri(BASE_URL + "/place")
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PlaceResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getName()).isEqualTo("update");
    }

    @Test
    void deletePlaceTest_shouldThrowServiceException_notFound() {
        PlaceRequest placeRequest = PlaceRequest.builder()
                .slug("test")
                .name("test")
                .language("ru")
                .timezone("UTC+8")
                .build();
        placeService.save(placeRequest);

        var expectedResponse = ErrorResponse.builder()
                .code(404)
                .text("Place not found")
                .build();

        var actualResponse = webClient.delete()
                .uri(BASE_URL + "/place/{id}", Long.MAX_VALUE)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    void deletePlaceTest_shouldDeletePlace_success() {
        PlaceRequest placeRequest = PlaceRequest.builder()
                .slug("test")
                .name("test")
                .language("ru")
                .timezone("UTC+8")
                .build();
        PlaceResponse placeResponse = placeService.save(placeRequest);

        webClient.delete()
                .uri(BASE_URL + "/place/{id}", placeResponse.getId())
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(placeService.findAll()).isEmpty();
    }

    @Test
    void getPlaceByIdTest_shouldThrowServiceException_notFound() {
        var expectedResponse = ErrorResponse.builder()
                .code(404)
                .text("Place not found")
                .build();

        var actualResponse = webClient.get()
                .uri(BASE_URL + "/place/{id}", Long.MAX_VALUE)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    void getPlaceByIdTest_shouldGetPlaceById_success() {

        PlaceRequest placeRequest = PlaceRequest.builder()
                .slug("test")
                .name("test")
                .language("ru")
                .timezone("UTC+8")
                .build();
        PlaceResponse placeResponse = placeService.save(placeRequest);

        PlaceResponse actualResponse = webClient.get()
                .uri(BASE_URL + "/place/{id}", placeResponse.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PlaceResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(placeResponse);
    }

    @AfterEach
    void cleanUp() {
        placeRepository.deleteAll();
    }

}
