package ru.gw3nax.kudagoapi.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gw3nax.kudagoapi.service.LocationService;

@Component
@RequiredArgsConstructor
public class LocationInitCommand implements InitCommand {

    private final LocationService locationService;

    @Override
    public void execute() {
        locationService.init();
    }
}
