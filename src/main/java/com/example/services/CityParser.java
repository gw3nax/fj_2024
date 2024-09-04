package main.java.com.example.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import main.java.com.example.entities.City;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class CityParser {
    private static final Logger logger = LoggerFactory.getLogger(CityParser.class);
    private static final XmlMapper xmlMapper = new XmlMapper();

    public static City readJson(String fileName) {
        logger.info("Reading data from file {}", fileName);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(fileName), City.class);
        }catch (IOException e){
            logger.error("Failed to read data from file {}", fileName);
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

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + ".xml"))) {
            logger.info("{}.xml has been written successfully.", fileName);
            String xmlString = xmlMapper.writeValueAsString(city);
            writer.write(xmlString);
        } catch (JsonProcessingException e) {
            logger.error("Failed to convert data to xml.");
        }catch (IOException e){
            logger.error("Failed write data to file {}", fileName);
        }
    }

    public static void main(String[] args) {
        logger.info("Application started");
        toXML("city");
        toXML("city-error");
        logger.info("Application ended");
    }
}
