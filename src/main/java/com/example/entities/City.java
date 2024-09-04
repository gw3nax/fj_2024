package main.java.com.example.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class City {
    @JsonProperty("slug")
    private String slug;
    @JsonProperty("coords")
    private Coords coords;
}