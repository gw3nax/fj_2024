package com.parser.services;

import com.parser.entities.City;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class JsonParserTest {

    @Test
    void testCityParserSuccess() throws URISyntaxException {
        URL resourceUrl = ClassLoader.getSystemResource("city.json");
        Path validResource = Paths.get(resourceUrl.toURI());
        City parsedObject = CityParser.readJson(validResource);
        assertThat(parsedObject).isNotNull();
    }

    @Test
    void testCityParserFailure() throws URISyntaxException {
        URL resourceUrl = ClassLoader.getSystemResource("city-error.json");
        Path validResource = Paths.get(resourceUrl.toURI());
        City parsedObject = CityParser.readJson(validResource);
        assertThat(parsedObject).isNull();
    }
}
