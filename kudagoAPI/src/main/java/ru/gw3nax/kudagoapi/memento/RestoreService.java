package ru.gw3nax.kudagoapi.memento;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gw3nax.kudagoapi.exception.ServiceException;
import ru.gw3nax.kudagoapi.memento.repository.EventSnapshotRepository;
import ru.gw3nax.kudagoapi.memento.repository.PlaceSnapshotRepository;
import ru.gw3nax.kudagoapi.repository.EventRepository;
import ru.gw3nax.kudagoapi.repository.PlaceRepository;

@Service
@RequiredArgsConstructor
public class RestoreService {
    EventRepository eventRepository;
    EventSnapshotRepository eventSnapshotRepository;
    PlaceRepository placeRepository;
    PlaceSnapshotRepository placeSnapshotRepository;

    @Transactional
    public void restoreEvent(Long eventId, Long eventSnapshotId) {
        var snapshot = eventSnapshotRepository.findById(eventSnapshotId)
                .orElseThrow(
                        () -> new ServiceException("Snapshot not found", 404)
                );
        var event = eventRepository.findById(eventId)
                .orElseThrow(
                        () -> new ServiceException("Event not found", 404)
                );
        var place = placeRepository.findById(snapshot.getPlaceId())
                .orElseThrow(
                        () -> new ServiceException("Place not found", 404)
                );


        event.setName(snapshot.getName());
        event.setDate(snapshot.getDate());
        event.setPlace(place);

        eventRepository.save(event);
    }

    @Transactional
    public void restorePlace(Long placeId, Long placeSnapshotId) {
        var snapshot = placeSnapshotRepository.findById(placeSnapshotId)
                .orElseThrow(
                        () -> new ServiceException("Place not found", 404)
                );
        var place = placeRepository.findById(placeId)
                .orElseThrow(
                () -> new ServiceException("Place not found", 404)
        );
        place.setLanguage(snapshot.getLanguage());
        place.setName(snapshot.getName());
        place.setSlug(snapshot.getSlug());
        place.setTimezone(snapshot.getTimezone());
        placeRepository.save(place);
    }
}
