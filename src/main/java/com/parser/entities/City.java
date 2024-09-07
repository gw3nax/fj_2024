package com.parser.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class City {
    @JsonProperty("slug")
    private String slug;
    @JsonProperty("coords")
    private Coords coords;
}