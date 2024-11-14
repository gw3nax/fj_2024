package ru.gw3nax.kudagoapi.controller.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
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
import ru.gw3nax.kudagoapi.repository.UserRepository;
import ru.gw3nax.kudagoapi.service.AuthenticationService;
import ru.gw3nax.kudagoapi.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ContextConfiguration(initializers = PostgresSqlInitializer.class)
class UserControllerIntegrationTest {

    @Container
    private static final WireMockContainer wireMockServer = new WireMockContainer("wiremock/wiremock:2.35.0");

    private static final String BASE_URL = "/auth";

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void registerUserSuccessTest() {
        var request = SignUpRequest.builder()
                .username("username")
                .password("password")
                .build();

        var response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URL + "/sign-up")
                        .build())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult().getResponseBody();

        assertThat(response).isNotNull();
    }

    @Test
    void registerUserFailureTest() {


        var request = SignUpRequest.builder()
                .username("username")
                .password("password")
                .build();

        var expectedResponse = ErrorResponse.builder()
                .text("Пользователь с таким именем уже существует")
                .code(400)
                .build();

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URL + "/sign-up")
                        .build())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();

        var actualResponse = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URL + "/sign-up")
                        .build())
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void authUserSuccessTest() {
        var signUpRequest = SignUpRequest.builder()
                .username("username")
                .password("password")
                .build();

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URL + "/sign-up")
                        .build())
                .bodyValue(signUpRequest)
                .exchange()
                .expectStatus().isOk();

        var signInRequest = SignInRequest.builder()
                .username("username")
                .password("password")
                .rememberMe(true)
                .build();


        var actualResponse = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URL + "/sign-in")
                        .build())
                .bodyValue(signInRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtAuthenticationResponse.class)
                .returnResult().getResponseBody();

        assertThat(actualResponse).isNotNull();
    }

    @Test
    void authUserFailureTest() {
        var signUpRequest = SignUpRequest.builder()
                .username("username")
                .password("password")
                .build();

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URL + "/sign-up")
                        .build())
                .bodyValue(signUpRequest)
                .exchange()
                .expectStatus().isOk();

        var signInRequest = SignInRequest.builder()
                .username("username")
                .password("aaaaaaaaa")
                .rememberMe(true)
                .build();

        var expectedResponse = ErrorResponse.builder()
                .text("Incorrect password")
                .code(401)
                .build();

        var actualResponse = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URL + "/sign-in")
                        .build())
                .bodyValue(signInRequest)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void resetPasswordSuccessTest() {

        var signUpRequest = SignUpRequest.builder()
                .username("username")
                .password("old-password")
                .build();

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URL + "/sign-up")
                        .build())
                .bodyValue(signUpRequest)
                .exchange()
                .expectStatus().isOk();

        var resetPasswordRequest = ResetPasswordRequest.builder()
                .username("username")
                .oldPassword("old-password")
                .newPassword("new-password")
                .confirmCode("0000")
                .build();

        var expectedResponse = UserResponse.builder()
                .text("Password has reset successfully")
                .build();

        var actualResponse = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URL + "/reset-password")
                        .build())
                .bodyValue(resetPasswordRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .returnResult().getResponseBody();

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Invalid old password test")
    void resetPasswordFailureTest() {

        var signUpRequest = SignUpRequest.builder()
                .username("username")
                .password("password")
                .build();

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URL + "/sign-up")
                        .build())
                .bodyValue(signUpRequest)
                .exchange()
                .expectStatus().isOk();

        var resetPasswordRequest = ResetPasswordRequest.builder()
                .username("username")
                .oldPassword("incorrect-password")
                .newPassword("new-password")
                .confirmCode("0000")
                .build();

        var expectedResponse = ErrorResponse.builder()
                .text("Incorrect password")
                .code(401)
                .build();


        var actualResponse = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URL + "/reset-password")
                        .build())
                .bodyValue(resetPasswordRequest)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Invalid Confirm Code test")
    void resetPasswordFailureTest2() {

        var signUpRequest = SignUpRequest.builder()
                .username("username")
                .password("password")
                .build();

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URL + "/sign-up")
                        .build())
                .bodyValue(signUpRequest)
                .exchange()
                .expectStatus().isOk();

        var resetPasswordRequest = ResetPasswordRequest.builder()
                .username("username")
                .oldPassword("password")
                .newPassword("new-password")
                .confirmCode("1111")
                .build();

        var expectedResponse = ErrorResponse.builder()
                .text("Incorrect confirm code")
                .code(401)
                .build();

        var actualResponse = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URL + "/reset-password")
                        .build())
                .bodyValue(resetPasswordRequest)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void logoutUserSuccessTest() {

        var signUpRequest = SignUpRequest.builder()
                .username("username")
                .password("password")
                .build();

        var token = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URL + "/sign-up")
                        .build())
                .bodyValue(signUpRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtAuthenticationResponse.class)
                .returnResult().getResponseBody();

        var expectedResponse = UserResponse.builder()
                .text("Token has invalidated successfully")
                .build();

        var actualResponse = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_URL + "/logout")
                        .build())
                .header("Authorization", "Bearer " + token.getToken())
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .returnResult().getResponseBody();


        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/secured")
                        .build())
                .header("Authentication", "Bearer " + token.getToken())
                .exchange()
                .expectStatus().isForbidden();

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }
}
