package main.java.com.example.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Coords {
    @JsonProperty("lat")
    private double lat;
    @JsonProperty("lon")
    private double lon;
}
