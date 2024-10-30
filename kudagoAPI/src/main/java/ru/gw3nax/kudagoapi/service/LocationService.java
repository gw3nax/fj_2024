package ru.gw3nax.kudagoapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.gw3nax.customstarter.aspect.Profiling;
import ru.gw3nax.kudagoapi.client.LocationClient;
import ru.gw3nax.kudagoapi.controller.dto.LocationRequest;
import ru.gw3nax.kudagoapi.controller.dto.LocationResponse;
import ru.gw3nax.kudagoapi.entity.Location;
import ru.gw3nax.kudagoapi.mapper.KudaGoLocationMapper;
import ru.gw3nax.kudagoapi.mapper.LocationMapper;
import ru.gw3nax.kudagoapi.observer.EntityEvent;
import ru.gw3nax.kudagoapi.observer.EntityEventType;
import ru.gw3nax.kudagoapi.repository.LocationRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationClient locationClient;
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Profiling
    public Future<Void> init() {
        log.info("Thread: " + Thread.currentThread().getName());
        log.info("Initializing LocationService");
        locationClient.getAllEntities()
                .forEach(entity -> locationRepository.save(KudaGoLocationMapper.mapToLocation(entity)));
        log.info("Finished Initializing LocationService");
        return null;
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
        var location = locationMapper.mapToLocation(request);
        applicationEventPublisher.publishEvent(new EntityEvent<>(this, location, EntityEventType.CREATED));
        locationRepository.save(location);
    }

    public void updateLocation(Long id, LocationRequest request) {
        Optional<Location> optionalLocation = locationRepository.findById(id);
        if (optionalLocation.isPresent()) {
            Location location = optionalLocation.get();
            applicationEventPublisher.publishEvent(new EntityEvent<>(this, location, EntityEventType.UPDATED));
            location.setName(request.getName());
            location.setTimezone(request.getTimezone());
            location.setLanguage(request.getLanguage());
            location.setSlug(request.getSlug());
            locationRepository.update(id, location);
        } else throw new NoSuchElementException();
    }

    public void deleteLocation(Long id) {
        if (locationRepository.findById(id).isPresent()) {
            var location = locationRepository.findById(id).get();
            applicationEventPublisher.publishEvent(new EntityEvent<>(this, location, EntityEventType.DELETED));
            locationRepository.deleteById(id);
        } else throw new NoSuchElementException();
    }
}
