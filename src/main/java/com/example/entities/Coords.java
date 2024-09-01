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

    @Override
    public String toString() {
        return    "   <coords>\n" +
                "       <lat>" + lat + "</lat>\n" +
                "       <lon>" + lon + "</lon>\n" +
                "   </coords>\n";
    }
}
