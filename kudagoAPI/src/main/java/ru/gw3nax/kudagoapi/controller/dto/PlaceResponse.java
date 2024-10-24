package ru.gw3nax.kudagoapi.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceResponse {
    private Long id;
    private String slug;
    private String name;
    private String timezone;
    private String language;
}
