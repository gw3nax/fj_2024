package ru.gw3nax.currency_exchanger.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;
import ru.gw3nax.currency_exchanger.CurrencyExchangerApplication;
import ru.gw3nax.currency_exchanger.controller.dto.ErrorResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CurrencyExchangerApplication.class)
@AutoConfigureMockMvc
@Testcontainers
@TestPropertySource(properties = "app.cbr.cbr-base-url=aaaa")
class CBRClientTest {

    @Container
    private static final WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:2.35.0");
    private static String BASEURL = "/currencies/rates";
    private static String wireMockUrl;
    @Autowired
    protected WebTestClient webClient;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        wireMockUrl = String.format("http://%s:%d",
                wiremockServer.getHost(),
                wiremockServer.getMappedPort(8080));
    }

    @Test
    void getCurrency_shouldThrowCBRUnavailableException(){
        var expectedResponse = ErrorResponse.builder()
                .message("CBR service is unavailable")
                .code(503)
                .build();

        var actualResponse = webClient.get()
                .uri(BASEURL + "/USD")
                .exchange()
                .expectStatus()
                .is5xxServerError()
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();
        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }
}
