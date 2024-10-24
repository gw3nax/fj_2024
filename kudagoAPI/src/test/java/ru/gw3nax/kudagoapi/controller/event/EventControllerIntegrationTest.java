package ru.gw3nax.kudagoapi.controller.event;

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
import ru.gw3nax.kudagoapi.controller.dto.*;
import ru.gw3nax.kudagoapi.repository.EventRepository;
import ru.gw3nax.kudagoapi.repository.PlaceRepository;
import ru.gw3nax.kudagoapi.service.EventService;
import ru.gw3nax.kudagoapi.service.PlaceService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ContextConfiguration(initializers = PostgresSqlInitializer.class)
class EventControllerIntegrationTest {

    @Container
    private static final WireMockContainer wireMockServer = new WireMockContainer("wiremock/wiremock:2.35.0");

    private static final String BASE_URL = "/api/v2/events";

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Test
    void getEventByIdTest_shouldThrowServiceException_notFound() {

        var expectedResponse = ErrorResponse.builder()
                .code(404)
                .text("Event not found")
                .build();
        var actualResponse = webClient.get()
                .uri(BASE_URL + "/event/{id}", Long.MAX_VALUE)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();


        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    void getEventByIdTest_shouldGetEventById_success() {
        PlaceRequest placeRequest = PlaceRequest.builder()
                .slug("test")
                .name("test")
                .language("ru")
                .timezone("UTC+8")
                .build();
        PlaceResponse placeResponse = placeService.save(placeRequest);
        EventRequest eventRequest = EventRequest.builder()
                .date("2024-10-20")
                .placeId(placeResponse.getId())
                .name("test")
                .build();
        EventResponse eventResponse = eventService.save(eventRequest);

        var actualResponse = webClient.get()
                .uri(BASE_URL + "/event/{id}", eventResponse.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(EventResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(eventResponse);
    }


    @Test
    void createEventTest_shouldThrowServiceException_badRequest() {

        var expectedResponse = ErrorResponse.builder()
                .code(400)
                .text("Place id cannot be null")
                .build();

        EventRequest request = EventRequest.builder()
                .name("New Event")
                .date("2024-10-20")
                .placeId(null)
                .build();

        var actualResponse = webClient.post()
                .uri(BASE_URL)
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    void createEventTest_shouldCreateNewEvent_success() {

        PlaceRequest placeRequest = PlaceRequest.builder()
                .slug("test")
                .name("test")
                .language("ru")
                .timezone("UTC+8")
                .build();
        PlaceResponse placeResponse = placeService.save(placeRequest);
        EventRequest request = EventRequest.builder()
                .name("New Event")
                .date("2024-10-20")
                .placeId(placeResponse.getId())
                .build();

        webClient.post()
                .uri(BASE_URL)
                .bodyValue(request)
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(eventService.findAll()).hasSize(1);
    }

    @Test
    void updateEventTest_shouldThrowServiceException_notFound() {

        var expectedResponse = ErrorResponse.builder()
                .code(404)
                .text("Event not found")
                .build();

        EventRequest updateRequest = EventRequest.builder()
                .date("2024-10-20")
                .placeId(null)
                .name("Updated Event")
                .id(Long.MAX_VALUE)
                .build();

        var actualResponse = webClient.put()
                .uri(BASE_URL + "/event")
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    void updateEventTest_shouldUpdateEvent_success() {
        PlaceRequest placeRequest = PlaceRequest.builder()
                .slug("test")
                .name("test")
                .language("ru")
                .timezone("UTC+8")
                .build();
        PlaceResponse placeResponse = placeService.save(placeRequest);
        EventRequest eventRequest = EventRequest.builder()
                .date("2024-10-20")
                .placeId(placeResponse.getId())
                .name("test")
                .build();
        EventResponse eventResponse = eventService.save(eventRequest);

        EventRequest updateRequest = EventRequest.builder()
                .date("2024-10-20")
                .placeId(placeResponse.getId())
                .name("Updated Event")
                .id(eventResponse.getId())
                .build();

        var actualResponse = webClient.put()
                .uri(BASE_URL + "/event")
                .bodyValue(updateRequest)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(EventResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.getName()).isEqualTo("Updated Event");
    }

    @Test
    void deleteEventTest_shouldThrowServiceException_notFound() {

        var expectedResponse = ErrorResponse.builder()
                .code(404)
                .text("Event not found")
                .build();

        EventRequest eventRequest = EventRequest.builder()
                .date("2024-10-20")
                .placeId(null)
                .name("Updated Event")
                .id(Long.MAX_VALUE)
                .build();

        var actualResponse =  webClient.delete()
                .uri(BASE_URL + "/event/{id}", eventRequest.getId())
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    void deleteEventTest_shouldDeleteEvent_success() {
        PlaceRequest placeRequest = PlaceRequest.builder()
                .slug("test")
                .name("test")
                .language("ru")
                .timezone("UTC+8")
                .build();
        PlaceResponse placeResponse = placeService.save(placeRequest);
        EventRequest eventRequest = EventRequest.builder()
                .date("2024-10-20")
                .placeId(placeResponse.getId())
                .name("test")
                .build();
        EventResponse eventResponse = eventService.save(eventRequest);

        webClient.delete()
                .uri(BASE_URL + "/event/{id}", eventResponse.getId())
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(eventService.findAll()).isEmpty();
    }


    @AfterEach
    void cleanUp() {
        eventRepository.deleteAll();
        placeRepository.deleteAll();
    }
}
