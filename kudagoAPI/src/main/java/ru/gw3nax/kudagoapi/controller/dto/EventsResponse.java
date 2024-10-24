package ru.gw3nax.kudagoapi.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventsResponse {
    Double price;
    String site_url;
    String title;
    String description;
}
