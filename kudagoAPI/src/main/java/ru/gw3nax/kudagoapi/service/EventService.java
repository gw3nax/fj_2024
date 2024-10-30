package ru.gw3nax.kudagoapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gw3nax.kudagoapi.controller.dto.EventRequest;
import ru.gw3nax.kudagoapi.controller.dto.EventResponse;
import ru.gw3nax.kudagoapi.entity.Event;
import ru.gw3nax.kudagoapi.entity.Place;
import ru.gw3nax.kudagoapi.exception.ServiceException;
import ru.gw3nax.kudagoapi.mapper.EventMapper;
import ru.gw3nax.kudagoapi.memento.service.EventSnapshotService;
import ru.gw3nax.kudagoapi.observer.EntityEvent;
import ru.gw3nax.kudagoapi.observer.EntityEventType;
import ru.gw3nax.kudagoapi.repository.EventRepository;
import ru.gw3nax.kudagoapi.repository.PlaceRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final PlaceRepository placeRepository;
    private final EventSnapshotService eventSnapshotService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public EventResponse save(EventRequest eventRequest) {
        Place place;
        if (eventRequest.getPlaceId() != null) {
            place = validatePlace(eventRequest.getPlaceId());
        } else throw new ServiceException("Place id cannot be null", 400);
        Event event = Event.builder()
                .date(LocalDate.parse(eventRequest.getDate()))
                .id(eventRequest.getId())
                .name(eventRequest.getName())
                .place(place)
                .build();
        eventPublisher.publishEvent(new EntityEvent<>(this, event, EntityEventType.CREATED));
        eventSnapshotService.save(event);
        return EventMapper.mapToEventResponse(eventRepository.save(event));
    }

    @Transactional
    public EventResponse update(EventRequest eventRequest) {
        Event event = validateEvent(eventRequest.getId());
        Place place = validatePlace(eventRequest.getPlaceId());
        event.setName(eventRequest.getName());
        event.setPlace(place);
        event.setDate(LocalDate.parse(eventRequest.getDate()));
        eventPublisher.publishEvent(new EntityEvent<>(this, event, EntityEventType.UPDATED));
        eventSnapshotService.save(event);
        return EventMapper.mapToEventResponse(eventRepository.save(event));
    }

    @Transactional
    public void delete(Long id) {
        if (eventRepository.existsById(id)) {
            var event = eventRepository.findById(id).get();
            eventPublisher.publishEvent(new EntityEvent<>(this, event, EntityEventType.UPDATED));
            eventRepository.deleteById(id);
        } else throw new ServiceException("Event not found", 404);
    }

    @Transactional(readOnly = true)
    public List<EventResponse> findAllWithFilters(String name, Long place, String fromDate, String toDate) {
        List<Event> events = eventRepository.findAll(EventRepository.buildSpecification(name, place, fromDate, toDate));
        if (events == null || events.isEmpty()) {
            return new ArrayList<>();
        } else return events.stream()
                .map(EventMapper::mapToEventResponse)
                .toList();
    }

    @Transactional
    public List<EventResponse> findAll(){
        List<Event> events = eventRepository.findAll();
        if (events.isEmpty()) {
            return new ArrayList<>();
        } else return events.stream()
                .map(EventMapper::mapToEventResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EventResponse findById(long id) {
        Event event = validateEvent(id);
        return EventMapper.mapToEventResponse(event);
    }

    private Event validateEvent(Long id) {
        if (id == null) {
            throw new ServiceException("Event id is required", 400);
        }
        Optional<Event> event = eventRepository.findById(id);
        if (event.isPresent()) {
            return event.get();
        } else {
            throw new ServiceException("Event not found", 404);
        }
    }

    private Place validatePlace(Long id) {

        Optional<Place> place = placeRepository.findById(id);
        if (place.isPresent()) {
            return place.get();
        } else throw new ServiceException("Place not found", 400);
    }
}
