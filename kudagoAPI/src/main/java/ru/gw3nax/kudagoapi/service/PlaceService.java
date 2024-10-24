package ru.gw3nax.kudagoapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gw3nax.kudagoapi.controller.dto.PlaceRequest;
import ru.gw3nax.kudagoapi.controller.dto.PlaceResponse;
import ru.gw3nax.kudagoapi.entity.Place;
import ru.gw3nax.kudagoapi.exception.ServiceException;
import ru.gw3nax.kudagoapi.mapper.PlaceMapper;
import ru.gw3nax.kudagoapi.repository.PlaceRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;

    @Transactional
    public PlaceResponse save(PlaceRequest placeRequest) {
        Place place = new Place();
        validateRequest(placeRequest);
        place.setName(placeRequest.getName());
        place.setSlug(placeRequest.getSlug());
        place.setLanguage(placeRequest.getLanguage());
        place.setTimezone(placeRequest.getTimezone());
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
        return PlaceMapper.mapToPlaceResponse(placeRepository.save(place));
    }

    @Transactional
    public void delete(Long id) {
        if (placeRepository.existsById(id)) {
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
