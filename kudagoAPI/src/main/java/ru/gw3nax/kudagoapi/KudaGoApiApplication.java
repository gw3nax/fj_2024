package ru.gw3nax.kudagoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.gw3nax.kudagoapi.configuration.ApplicationConfig;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class KudaGoApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(KudaGoApiApplication.class, args);
    }
}