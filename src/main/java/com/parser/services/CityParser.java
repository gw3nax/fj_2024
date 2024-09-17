package com.parser.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.parser.entities.City;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

@Slf4j
public class CityParser {
    private static final XmlMapper xmlMapper = new XmlMapper();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static City readJson(Path path) {
        String fileName = path.getFileName().toString();
        log.debug("Reading data from file {}", fileName);
        try {
            return objectMapper.readValue(path.toFile(), City.class);
        } catch (IOException e) {
            log.error("Failed to read data from file {}", fileName);
            return null;
        }
    }

    public static Path toXML(Path path) {
        City city = readJson(path);
        String fileName = path.getFileName().toString();
        if (city == null) {
            log.warn("Error reading {}.json.", fileName);
            return null;
        } else {
            log.debug("Data has read successfully.");
            log.info("Parsed object: {}", city);
        }
        String resultFileName = Stream.of(fileName)
                .map(name -> name.replaceAll("\\.json$", ".xml"))
                .findFirst()
                .orElse(fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultFileName))) {
            String xmlString = xmlMapper.writeValueAsString(city);
            writer.write(xmlString);
            log.debug("{}.xml has been written successfully.", fileName);
            return Path.of(resultFileName);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert data to xml.");
            return null;
        } catch (IOException e) {
            log.error("Failed write data to file {}", fileName);
            return null;
        }
    }
}