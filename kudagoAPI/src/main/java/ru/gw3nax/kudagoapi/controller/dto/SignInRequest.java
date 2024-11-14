package ru.gw3nax.kudagoapi.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInRequest {
    private String username;
    private String password;
    private Boolean rememberMe;
}