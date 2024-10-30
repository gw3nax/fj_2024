package ru.gw3nax.kudagoapi.memento.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.gw3nax.kudagoapi.entity.Place;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "events_snapshot")
public class EventSnapshot extends Snapshot {
    String name;
    LocalDate date;
    Long placeId;
}
