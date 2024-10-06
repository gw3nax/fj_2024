package ru.gw3nax.kudagoAPI.mapper;

import org.springframework.stereotype.Component;
import ru.gw3nax.kudagoAPI.client.dto.KudaGoLocationResponse;
import ru.gw3nax.kudagoAPI.entity.Location;

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
