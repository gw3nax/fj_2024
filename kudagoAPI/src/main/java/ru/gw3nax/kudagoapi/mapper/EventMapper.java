package ru.gw3nax.kudagoapi.mapper;

import ru.gw3nax.kudagoapi.client.dto.KudaGoEventResponse;
import ru.gw3nax.kudagoapi.controller.dto.EventsResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventMapper {
    public static EventsResponse mapToEventsResponse(KudaGoEventResponse kudaGoEventResponse) {
        EventsResponse eventsResponse = new EventsResponse();
        eventsResponse.setDescription(kudaGoEventResponse.getDescription());
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(kudaGoEventResponse.getPrice() != null ? kudaGoEventResponse.getPrice().toString() : "0");

        if (matcher.find()) {
            eventsResponse.setPrice(Double.valueOf(matcher.group()));
        } else {
            eventsResponse.setPrice(0.0);
        }

        eventsResponse.setTitle(kudaGoEventResponse.getTitle());
        eventsResponse.setSite_url(kudaGoEventResponse.getSite_url());
        return eventsResponse;
    }
}
