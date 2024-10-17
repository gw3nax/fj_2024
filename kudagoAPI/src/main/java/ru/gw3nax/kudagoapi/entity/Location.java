package ru.gw3nax.kudagoapi.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    Long id;
    String slug;
    String name;
    String timezone;
    String language;
}
