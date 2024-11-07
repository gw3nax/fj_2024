package ru.gw3nax.kudagoapi.controller.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ResetPasswordRequest {
    private String username;
    private String oldPassword;
    private String newPassword;
    private String confirmCode;
}
