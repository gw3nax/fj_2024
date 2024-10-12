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
import ru.gw3nax.kudagoAPI.controller.dto.CategoryRequest;
import ru.gw3nax.kudagoAPI.controller.dto.CategoryResponse;
import ru.gw3nax.kudagoAPI.entity.Category;
import ru.gw3nax.kudagoAPI.mapper.CategoryMapper;
import ru.gw3nax.kudagoAPI.repository.CategoryRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class CategoryControllerIntegrationTest {

    @Container
    private static final WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:2.35.0")
            .withMappingFromResource("wiremock/categorySetup.json")
            .withMappingFromResource("wiremock/locationSetup.json");
    private static String BASEURL = "/api/v1/places/categories";
    private static String wireMockUrl;
    @Autowired
    protected WebTestClient webClient;
    @Autowired
    protected CategoryRepository categoryRepository;
    @Autowired
    protected CategoryMapper categoryMapper;
    @LocalServerPort
    private int port;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        wireMockUrl = String.format("http://%s:%d",
                wiremockServer.getHost(),
                wiremockServer.getMappedPort(8080));
        registry.add("kudago.api.category-base-url", () -> wireMockUrl + "/public-api/v1.4/locations");
    }

    @Test
    void getCategoryByIdTest() {
        Category category = new Category(1L, "slug", "name");
        categoryRepository.save(category);

        CategoryResponse expectedResponse = categoryMapper.mapToCategoryResponse(category);

        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path(BASEURL + "/{id}").build(category.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CategoryResponse.class)
                .returnResult()
                .getResponseBody();
        assertThat(response).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    void postCategoryTest() {
        CategoryRequest request = new CategoryRequest(1L, "slug", "name");

        var expectedSize = categoryRepository.findAll().size();
        webClient.post()
                .uri(BASEURL)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();

        Category savedCategory = categoryRepository.findById((long) expectedSize).get();
        assertThat(savedCategory.getSlug()).isEqualTo("slug");
        assertThat(savedCategory.getName()).isEqualTo("name");
    }

    @Test
    void updateCategoryTest() {
        var category = new Category(1L, "slug", "name");
        categoryRepository.save(category);
        var requestForUpdate = new CategoryRequest();
        requestForUpdate.setName("update");
        requestForUpdate.setSlug("update");

        webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path(BASEURL + "/{id}")
                        .build(category.getId()))
                .bodyValue(requestForUpdate)
                .exchange();

        var updatedCategory = categoryRepository.findById(category.getId()).get();
        assertThat(updatedCategory.getName()).isEqualTo(requestForUpdate.getName());
        assertThat(updatedCategory.getSlug()).isEqualTo(requestForUpdate.getSlug());
    }

    @Test
    void deleteCategoryTest() {
        var category = new Category(1L, "slug", "name");
        categoryRepository.save(category);

        webClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path(BASEURL + "/{id}")
                        .build(category.getId()))
                .exchange()
                .expectStatus().isOk();

        assertThat(categoryRepository.findById(category.getId())).isEmpty();
    }
    @AfterEach
    void cleanUp() {
        categoryRepository.clearAll();
    }
}
