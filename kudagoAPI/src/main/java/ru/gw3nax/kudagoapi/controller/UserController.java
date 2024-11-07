package ru.gw3nax.kudagoapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gw3nax.kudagoapi.controller.dto.*;
import ru.gw3nax.kudagoapi.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody SignInRequest request) {
        return authenticationService.signIn(request);
    }
    @PostMapping("/reset-password")
    public UserResponse resetPassword(@RequestBody ResetPasswordRequest request) {
        return authenticationService.resetPassword(request);
    }

    @PostMapping("/logout")
    public UserResponse logout(HttpServletRequest request) {
        return authenticationService.logout(request);
    }
}
