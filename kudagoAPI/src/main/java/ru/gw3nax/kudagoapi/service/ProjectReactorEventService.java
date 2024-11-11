package ru.gw3nax.kudagoapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.gw3nax.kudagoapi.client.CurrencyClient;
import ru.gw3nax.kudagoapi.client.EventsClient;
import ru.gw3nax.kudagoapi.client.dto.CurrencyRequest;
import ru.gw3nax.kudagoapi.client.dto.KudaGoEventResponse;
import ru.gw3nax.kudagoapi.controller.dto.EventsResponse;
import ru.gw3nax.kudagoapi.mapper.EventMapper;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Profile("reactor")
public class ProjectReactorEventService implements EventService {
    private final CurrencyClient currencyClient;
    private final EventsClient eventsClient;

    @Override
    public Mono<List<EventsResponse>> getEvents(Integer budget, String currency, LocalDate dateFrom, LocalDate dateTo) {
        log.info("Project Reactor Method is invoked");
        Mono<Double> convertedBudgetMono = Mono.fromCallable(() ->currencyClient.postForConvertCurrency(new CurrencyRequest(currency, "RUB", (double) budget)));

        Mono<List<KudaGoEventResponse>> eventsMono = Mono.defer(
                () -> Mono.just(eventsClient.getAllEntities(dateFrom.atStartOfDay().toEpochSecond(ZoneOffset.UTC),
                        dateTo.atStartOfDay().toEpochSecond(ZoneOffset.UTC)))
        );

        return Mono.zip(eventsMono, convertedBudgetMono)
                .flatMap(tuple -> {
                    List<KudaGoEventResponse> events = tuple.getT1();
                    Double convertedBudget = tuple.getT2();

                    List<EventsResponse> filteredEvents = events.stream()
                            .map(EventMapper::mapToEventsResponse)
                            .filter(event -> event.getPrice() <= convertedBudget)
                            .collect(Collectors.toList());

                    return Mono.just(filteredEvents);
                });
    }
}
