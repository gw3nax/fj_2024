package com.parser.services;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class XmlParserTest {

    @Test
    void testToXmlSuccess() throws URISyntaxException {
        URL resourceUrl = ClassLoader.getSystemResource("city.json");
        Path validResource = Paths.get(resourceUrl.toURI());
        Path path = CityParser.toXML(validResource);
        assertThat(path).isNotNull();
    }

    @Test
    void testToXmlFailure() throws URISyntaxException {
        URL resourceUrl = ClassLoader.getSystemResource("city-error.json");
        Path validResource = Paths.get(resourceUrl.toURI());
        Path path = CityParser.toXML(validResource);
        assertThat(path).isNull();
    }
}
