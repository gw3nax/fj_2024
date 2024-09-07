package com.parser.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Coords {
    @JsonProperty("lat")
    private double lat;
    @JsonProperty("lon")
    private double lon;
}