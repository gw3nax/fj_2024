package main.java.com.example.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.example.entities.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class CityParser {
    private static final Logger logger = LoggerFactory.getLogger(CityParser.class);

    public static City readJson(String fileName) {
        logger.info("Reading data from file {}", fileName);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(fileName), City.class);
        }catch (IOException e){
            logger.error(e.getMessage());
            return null;
        }
    }

    public static void toXML(String fileName) {
        City city = readJson(fileName + ".json");
        if (city == null) {
            logger.error("Error reading {}.json.", fileName);
            return;
        } else{
            logger.info("Data has readed successfully.");
        }
        String cityXML = city.toString();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + ".xml"))) {
            writer.write(cityXML);
            logger.info("{}.xml has been written successfully.", fileName);
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }

    public static void main(String[] args) {
        logger.info("Application started");
        toXML("city");
        toXML("city-error");
        logger.info("Application ended");
    }
}
