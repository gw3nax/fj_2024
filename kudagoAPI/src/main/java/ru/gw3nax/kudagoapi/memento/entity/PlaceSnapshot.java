package ru.gw3nax.kudagoapi.memento.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.gw3nax.kudagoapi.entity.Event;

import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "places_snapshot")
public class PlaceSnapshot extends Snapshot {
    String slug;
    String name;
    String timezone;
    String language;
}
