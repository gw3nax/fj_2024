package ru.gw3nax.kudagoapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.gw3nax.kudagoapi.client.CurrencyClient;
import ru.gw3nax.kudagoapi.client.EventsClient;
import ru.gw3nax.kudagoapi.client.dto.CurrencyRequest;
import ru.gw3nax.kudagoapi.client.dto.KudaGoEventResponse;
import ru.gw3nax.kudagoapi.controller.dto.EventsResponse;
import ru.gw3nax.kudagoapi.exception.ServiceException;
import ru.gw3nax.kudagoapi.mapper.EventMapper;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Profile("future")
public class CompletableFutureEventService implements MultithreadingEventService {
    private final CurrencyClient currencyClient;
    private final EventsClient eventsClient;
    AtomicReference<List<EventsResponse>> filteredEvents = new AtomicReference<>();

    @Override
    public CompletableFuture<List<EventsResponse>> getEvents(Integer budget, String currency, LocalDate dateFrom, LocalDate dateTo) {
        log.info("Completable Future Method is invoked");

        CompletableFuture<Double> convertedBudgetFuture = CompletableFuture.supplyAsync(() ->
                        currencyClient.postForConvertCurrency(new CurrencyRequest(currency, "RUB", (double) budget)))
                .exceptionally(ex -> {
                    throw new ServiceException("Failed to convert currency", 500);
                });

        CompletableFuture<List<KudaGoEventResponse>> eventsFuture = CompletableFuture.supplyAsync(() ->
                        eventsClient.getAllEntities(dateFrom.atStartOfDay().toEpochSecond(ZoneOffset.UTC), dateTo.atStartOfDay().toEpochSecond(ZoneOffset.UTC)))
                .exceptionally(ex -> {
                    log.error("Error in getting events", ex);
                    return Collections.emptyList();
                });

        return eventsFuture.thenAcceptBoth(convertedBudgetFuture, (events, convertedBudget) ->
                filteredEvents.set(events.stream()
                        .map(EventMapper::mapToEventsResponse)
                        .filter(event -> event.getPrice().compareTo(convertedBudget) <= 0)
                        .collect(Collectors.toList()))).thenApply(v -> filteredEvents.get());
    }
}
