package ru.gw3nax.kudagoapi.service.location;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import ru.gw3nax.kudagoapi.client.LocationClient;
import ru.gw3nax.kudagoapi.client.dto.KudaGoLocationResponse;
import ru.gw3nax.kudagoapi.controller.dto.LocationRequest;
import ru.gw3nax.kudagoapi.controller.dto.LocationResponse;
import ru.gw3nax.kudagoapi.entity.Location;
import ru.gw3nax.kudagoapi.mapper.LocationMapper;
import ru.gw3nax.kudagoapi.repository.LocationRepository;
import ru.gw3nax.kudagoapi.service.LocationService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LocationServiceUnitTest {

    @Mock
    private LocationClient locationClient;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private LocationMapper locationMapper;

    @InjectMocks
    private LocationService locationService;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void init_shouldInitializeLocations() {
        // Arrange
        List<KudaGoLocationResponse> entities = List.of(new KudaGoLocationResponse());
        when(locationClient.getAllEntities()).thenReturn(entities);
        doNothing().when(locationRepository).save(any(Location.class));

        // Act
        locationService.init();

        // Assert
        verify(locationClient, times(1)).getAllEntities();
        verify(locationRepository, times(1)).save(any(Location.class));
    }

    @Test
    void getLocations_shouldReturnListOfLocationResponses() {
        // Arrange
        List<Location> locations = List.of(new Location(1L, "slug", "name", "timezone", "language"));
        when(locationRepository.findAll()).thenReturn(locations);
        when(locationMapper.mapToLocationResponse(any(Location.class)))
                .thenReturn(new LocationResponse(1L, "slug", "name", "timezone", "language"));

        // Act
        List<LocationResponse> result = locationService.getLocations();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("slug", result.get(0).getSlug());
        assertEquals("name", result.get(0).getName());
    }

    @Test
    void getLocationById_shouldReturnLocationResponse_whenLocationExists() {
        // Arrange
        Location location = new Location(1L, "slug", "name", "timezone", "language");
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(locationMapper.mapToLocationResponse(location))
                .thenReturn(new LocationResponse(1L, "slug", "name", "timezone", "language"));

        // Act
        LocationResponse result = locationService.getLocationById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(location.getId(), result.getId());
        assertEquals(location.getSlug(), result.getSlug());
        assertEquals(location.getName(), result.getName());
    }

    @Test
    void getLocationById_shouldReturnNull_whenLocationDoesNotExist() {
        // Arrange
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        LocationResponse result = locationService.getLocationById(1L);

        // Assert
        assertNull(result);
    }

    @Test
    void postLocation_shouldSaveLocation() {
        // Arrange
        LocationRequest request = new LocationRequest(1L, "slug", "name", "timezone", "language");
        Location location = new Location(1L, "slug", "name", "timezone", "language");
        when(locationMapper.mapToLocation(request)).thenReturn(location);

        // Act
        locationService.postLocation(request);

        // Assert
        verify(locationRepository, times(1)).save(location);
    }

    @Test
    void updateLocation_shouldUpdateLocation_whenLocationExists() {
        // Arrange
        Location location = new Location(1L, "old-slug", "old-name", "old-timezone", "old-language");
        LocationRequest request = new LocationRequest(1L, "slug", "name", "timezone", "language");

        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        // Act
        locationService.updateLocation(1L, request);

        // Assert
        verify(locationRepository, times(1)).update(location.getId(),location);
        assertEquals(request.getName(), location.getName());
        assertEquals(request.getTimezone(), location.getTimezone());
        assertEquals(request.getLanguage(), location.getLanguage());
        assertEquals(request.getSlug(), location.getSlug());
    }

    @Test
    void deleteLocation_shouldDeleteLocation_whenLocationExists() {
        // Arrange
        Location location = new Location(1L, "slug", "name", "timezone", "language");
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        // Act
        locationService.deleteLocation(1L);

        // Assert
        verify(locationRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteLocation_shouldThrowException_whenLocationDoesNotExist() {
        // Arrange
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        // Assert
        assertThrows(NoSuchElementException.class, () -> locationService.deleteLocation(1L));
    }
}
