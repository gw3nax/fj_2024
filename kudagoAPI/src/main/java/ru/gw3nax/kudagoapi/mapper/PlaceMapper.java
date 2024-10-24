package ru.gw3nax.kudagoapi.mapper;

import ru.gw3nax.kudagoapi.controller.dto.PlaceResponse;
import ru.gw3nax.kudagoapi.entity.Place;

public class PlaceMapper {
    public static PlaceResponse mapToPlaceResponse(Place place) {
        return PlaceResponse.builder()
                .id(place.getId())
                .slug(place.getSlug())
                .language(place.getLanguage())
                .timezone(place.getTimezone())
                .name(place.getName())
                .id(place.getId())
                .build();
    }
}
