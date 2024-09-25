package ru.gw3nax.kudagoAPI.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gw3nax.customstarter.PostProxy.PostProxy;
import ru.gw3nax.customstarter.aspect.Profiling;
import ru.gw3nax.kudagoAPI.client.LocationClient;
import ru.gw3nax.kudagoAPI.controller.dto.LocationRequest;
import ru.gw3nax.kudagoAPI.controller.dto.LocationResponse;
import ru.gw3nax.kudagoAPI.entity.Location;
import ru.gw3nax.kudagoAPI.mapper.KudaGoLocationMapper;
import ru.gw3nax.kudagoAPI.mapper.LocationMapper;
import ru.gw3nax.kudagoAPI.repository.LocationRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationClient locationClient;
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Profiling
    @PostProxy
    public void init() {
        log.info("Initializing LocationService");
        locationClient.getAllEntities()
                .forEach(entity -> locationRepository.save(KudaGoLocationMapper.mapToLocation(entity)));
        log.info("Finished Initializing LocationService");
    }

    public List<LocationResponse> getLocations() {
        List<Location> locations = locationRepository.findAll();
        return locations.stream()
                .map(locationMapper::mapToLocationResponse)
                .toList();
    }

    public LocationResponse getLocationById(Long id) {
        Optional<Location> location = locationRepository.findById(id);
        return location.map(locationMapper::mapToLocationResponse).orElse(null);
    }

    public void postLocation(LocationRequest request) {
        locationRepository.save(locationMapper.mapToLocation(request));
    }

    public void updateLocation(Long id, LocationRequest request) {
        Optional<Location> optionalLocation = locationRepository.findById(id);
        locationRepository.deleteById(id);
        if (optionalLocation.isPresent()) {
            Location location = optionalLocation.get();
            location.setName(request.getName());
            location.setTimezone(request.getTimezone());
            location.setLanguage(request.getLanguage());
            location.setSlug(request.getSlug());
            locationRepository.save(location);
        }
    }

    public void deleteLocation(Long id) {
        if (locationRepository.findById(id).isPresent()) {
            locationRepository.deleteById(id);
        } else throw new NoSuchElementException();
    }
}
