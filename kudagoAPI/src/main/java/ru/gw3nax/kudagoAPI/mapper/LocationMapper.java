package ru.gw3nax.kudagoAPI.mapper;

import org.springframework.stereotype.Component;
import ru.gw3nax.kudagoAPI.controller.dto.LocationRequest;
import ru.gw3nax.kudagoAPI.controller.dto.LocationResponse;
import ru.gw3nax.kudagoAPI.entity.Location;

@Component
public class LocationMapper {

    public LocationResponse mapToLocationResponse(Location location) {
        LocationResponse locationResponse = new LocationResponse();
        locationResponse.setId(location.getId());
        locationResponse.setLanguage(location.getLanguage());
        locationResponse.setSlug(location.getSlug());
        locationResponse.setTimezone(location.getTimezone());
        locationResponse.setName(location.getName());
        return locationResponse;
    }

    public Location mapToLocation(LocationRequest locationRequest) {
        Location location = new Location();
        location.setId(locationRequest.getId());
        location.setLanguage(locationRequest.getLanguage());
        location.setSlug(locationRequest.getSlug());
        location.setTimezone(locationRequest.getTimezone());
        location.setName(locationRequest.getName());
        return location;
    }
}
