package ru.gw3nax.kudagoapi.memento.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gw3nax.kudagoapi.entity.Place;
import ru.gw3nax.kudagoapi.memento.entity.PlaceSnapshot;
import ru.gw3nax.kudagoapi.memento.repository.PlaceSnapshotRepository;

@Service
@RequiredArgsConstructor
public class PlaceSnapshotService {
    private final PlaceSnapshotRepository placeSnapshotRepository;

    public void save(Place place){
        var snapshot = PlaceSnapshot.builder()
                .name(place.getName())
                .slug(place.getSlug())
                .timezone(place.getTimezone())
                .language(place.getLanguage())
                .build();
        placeSnapshotRepository.save(snapshot);
    }
}
