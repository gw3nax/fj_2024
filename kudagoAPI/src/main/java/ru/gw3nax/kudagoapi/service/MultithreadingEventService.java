package ru.gw3nax.kudagoapi.service;

import java.time.LocalDate;

public interface MultithreadingEventService<T> {
    T getEvents(Integer budget, String currency, LocalDate dateFrom, LocalDate dateTo);
}
