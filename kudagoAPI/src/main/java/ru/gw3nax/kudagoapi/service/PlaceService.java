package ru.gw3nax.kudagoapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gw3nax.kudagoapi.controller.dto.PlaceRequest;
import ru.gw3nax.kudagoapi.controller.dto.PlaceResponse;
import ru.gw3nax.kudagoapi.entity.Place;
import ru.gw3nax.kudagoapi.exception.ServiceException;
import ru.gw3nax.kudagoapi.mapper.PlaceMapper;
import ru.gw3nax.kudagoapi.memento.service.EventSnapshotService;
import ru.gw3nax.kudagoapi.memento.service.PlaceSnapshotService;
import ru.gw3nax.kudagoapi.observer.EntityEvent;
import ru.gw3nax.kudagoapi.observer.EntityEventType;
import ru.gw3nax.kudagoapi.repository.PlaceRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PlaceSnapshotService placeSnapshotService;

    @Transactional
    public PlaceResponse save(PlaceRequest placeRequest) {
        validateRequest(placeRequest);
        Place place = Place.builder()
                .name(placeRequest.getName())
                .slug(placeRequest.getSlug())
                .language(placeRequest.getLanguage())
                .timezone(placeRequest.getTimezone())
                .build();
        eventPublisher.publishEvent(new EntityEvent<>(this, place, EntityEventType.CREATED));
        placeSnapshotService.save(place);
        return PlaceMapper.mapToPlaceResponse(placeRepository.save(place));
    }

    private void validateRequest(PlaceRequest placeRequest) {
        if (placeRequest.getName() == null || placeRequest.getSlug() == null) {
            throw new ServiceException("Invalid request properties", 400);
        }
    }

    private Place validatePlace(Long id) {
        if (id == null) {
            throw new ServiceException("Place id cannot be null", 400);
        }
        Optional<Place> place = placeRepository.findByIdWithQuery(id);
        if (place.isPresent()) {
            return place.get();
        } else throw new ServiceException("Place not found", 404);
    }

    @Transactional
    public PlaceResponse update(PlaceRequest placeRequest) {
        Place place = validatePlace(placeRequest.getId());
        place.setName(placeRequest.getName());
        place.setSlug(placeRequest.getSlug());
        place.setLanguage(placeRequest.getLanguage());
        place.setTimezone(placeRequest.getTimezone());
        eventPublisher.publishEvent(new EntityEvent<>(this, place, EntityEventType.UPDATED));
        placeSnapshotService.save(place);
        return PlaceMapper.mapToPlaceResponse(placeRepository.save(place));
    }

    @Transactional
    public void delete(Long id) {
        if (placeRepository.existsById(id)) {
            var place = placeRepository.findById(id).get();
            eventPublisher.publishEvent(new EntityEvent<>(this, place, EntityEventType.UPDATED));
            placeRepository.deleteById(id);
        } else throw new ServiceException("Place not found", 404);
    }

    @Transactional
    public List<PlaceResponse> findAll() {
        return placeRepository.findAll().stream()
                .map(PlaceMapper::mapToPlaceResponse)
                .toList();
    }

    @Transactional
    public PlaceResponse findById(long id) {
        Place place = validatePlace(id);
        return PlaceMapper.mapToPlaceResponse(place);
    }
}
