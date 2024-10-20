package ru.gw3nax.kudagoapi.mapper;

import org.springframework.stereotype.Component;
import ru.gw3nax.kudagoapi.client.dto.KudaGoLocationResponse;
import ru.gw3nax.kudagoapi.entity.Location;

@Component
public class KudaGoLocationMapper {
    public static Location mapToLocation(KudaGoLocationResponse kudaGoLocationResponse) {
        Location location = new Location();
        location.setLanguage(kudaGoLocationResponse.getLanguage());
        location.setName(kudaGoLocationResponse.getName());
        location.setSlug(kudaGoLocationResponse.getSlug());
        location.setTimezone(kudaGoLocationResponse.getTimezone());
        return location;
    }
}
