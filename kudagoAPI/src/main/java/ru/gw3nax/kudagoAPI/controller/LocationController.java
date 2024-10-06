package ru.gw3nax.kudagoAPI.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gw3nax.customstarter.aspect.Profiling;
import ru.gw3nax.kudagoAPI.controller.dto.LocationRequest;
import ru.gw3nax.kudagoAPI.controller.dto.LocationResponse;
import ru.gw3nax.kudagoAPI.service.LocationService;

import java.util.List;

@Profiling
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/locations")
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public List<LocationResponse> getLocations() {
        return locationService.getLocations();
    }

    @GetMapping("/{id}")
    public LocationResponse getLocation(@PathVariable Long id) {
        return locationService.getLocationById(id);
    }

    @PostMapping
    public void postLocation(@RequestBody LocationRequest locationRequest) {
        locationService.postLocation(locationRequest);
    }

    @PutMapping("/{id}")
    public void updateLocation(@PathVariable Long id, @RequestBody LocationRequest locationRequest) {
        locationService.updateLocation(id, locationRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
    }
}
