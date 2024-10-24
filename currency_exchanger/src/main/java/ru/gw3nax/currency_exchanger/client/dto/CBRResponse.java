package ru.gw3nax.currency_exchanger.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CBRResponse {
    @JacksonXmlProperty(localName = "CharCode")
    String charCode;
    @JacksonXmlProperty(localName = "VunitRate")
    String value;
}
