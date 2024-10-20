package ru.gw3nax.kudagoapi.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KudaGoEventsResponseWrapper {
    Integer count;
    List<KudaGoEventResponse> results;
}
