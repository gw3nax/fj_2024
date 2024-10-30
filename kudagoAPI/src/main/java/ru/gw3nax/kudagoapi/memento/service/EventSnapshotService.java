package ru.gw3nax.kudagoapi.memento.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gw3nax.kudagoapi.entity.Event;
import ru.gw3nax.kudagoapi.memento.entity.EventSnapshot;
import ru.gw3nax.kudagoapi.memento.repository.EventSnapshotRepository;

@Service
@RequiredArgsConstructor
public class EventSnapshotService {
    private final EventSnapshotRepository eventSnapshotRepository;

    public void save(Event event){
        var snapshot = EventSnapshot.builder()
                .date(event.getDate())
                .placeId(event.getPlace().getId())
                .name(event.getName())
                .build();
        eventSnapshotRepository.save(snapshot);
    }
}
