package ru.gw3nax.kudagoapi.service;

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
import ru.gw3nax.currency_exchanger.CurrencyExchangerApplication;
import ru.gw3nax.currency_exchanger.controller.dto.ConvertCurrencyRequest;
import ru.gw3nax.currency_exchanger.controller.dto.ConvertCurrencyResponse;
import ru.gw3nax.currency_exchanger.controller.dto.CurrencyResponse;
import ru.gw3nax.currency_exchanger.controller.dto.ErrorResponse;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CurrencyExchangerApplication.class)
@AutoConfigureMockMvc
@Testcontainers
class CurrencyIntegrationTest {

    @Container
    private static final WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:2.35.0")
            .withMappingFromResource("wiremock/currency.json")
            .withFile(new File("src/test/resources/files/currency.xml"));

    private static String BASEURL = "/currencies";
    private static String wireMockUrl;

    @Autowired
    protected WebTestClient webClient;

    @LocalServerPort
    private int port;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        wireMockUrl = String.format("http://%s:%d",
                wiremockServer.getHost(),
                wiremockServer.getMappedPort(8080));
        registry.add("app.cbr.cbr-base-url", () -> wireMockUrl + "/scripts/XML_daily.asp");
    }

    @Test
    void getCurrencyByCharCodeTestSuccess() {
        var expectedResponse = new CurrencyResponse("AZN", 56.5341);

        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASEURL + "/rates/{charCode}").build(expectedResponse.getCharCode()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CurrencyResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(response).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    void getCurrencyByCharCodeTestNotFound() {
        var errorResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASEURL + "/rates/{charCode}").build("THB"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(errorResponse.getCode()).isEqualTo(404);
    }

    @Test
    void getCurrencyByCharCodeTestNotExistingValute() {
        var errorResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASEURL + "/rates/{charCode}").build("AAA"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(errorResponse.getCode()).isEqualTo(400);
    }

    @Test
    void convertCurrenciesTestSuccess() {
        var request = new ConvertCurrencyRequest("AUD", "AZN", 100.0);
        var expectedResponse = new ConvertCurrencyResponse("AUD", "AZN", 114.56);

        var response = webClient.post()
                .uri(uriBuilder -> uriBuilder.path(BASEURL + "/convert").build())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ConvertCurrencyResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(response).usingRecursiveComparison().isEqualTo(expectedResponse);
    }


    @Test
    void convertCurrenciesTestNotFound() {
        var request = new ConvertCurrencyRequest("THB", "AZN", 100.0);

        var errorResponse = webClient.post()
                .uri(uriBuilder -> uriBuilder.path(BASEURL + "/convert").build())
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(errorResponse.getCode()).isEqualTo(404);
    }

    @Test
    void convertCurrenciesTestNotExistingValute() {
        var request = new ConvertCurrencyRequest("AAA", "AZN", 100.0);

        var errorResponse = webClient.post()
                .uri(uriBuilder -> uriBuilder.path(BASEURL + "/convert").build())
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult()
                .getResponseBody();
        assertThat(errorResponse.getCode()).isEqualTo(400);
    }
}

