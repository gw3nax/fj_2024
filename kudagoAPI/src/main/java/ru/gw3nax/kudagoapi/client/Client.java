package ru.gw3nax.kudagoapi.client;

import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public interface Client<T> {
    public List<T> getAllEntities();
}
