package ru.gw3nax.kudagoapi.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gw3nax.kudagoapi.controller.dto.*;
import ru.gw3nax.kudagoapi.entity.Role;
import ru.gw3nax.kudagoapi.entity.UserEntity;
import ru.gw3nax.kudagoapi.exception.IncorrectConfirmCodeException;
import ru.gw3nax.kudagoapi.exception.IncorrectPasswordException;
import ru.gw3nax.kudagoapi.exception.ServiceException;
import ru.gw3nax.kudagoapi.security.BlackList;
import ru.gw3nax.kudagoapi.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final BlackList blackList;

    private final String CONFIRM_CODE = "0000";

    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        var user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userService.create(user);

        var jwt = jwtService.generateToken(user, false);
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            ));
        } catch (BadCredentialsException e) {
            throw new IncorrectPasswordException("Incorrect password");
        }

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user, request.getRememberMe());
        return new JwtAuthenticationResponse(jwt);
    }

    public UserResponse resetPassword(ResetPasswordRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getOldPassword()
            ));
        } catch (BadCredentialsException e) {
            throw new IncorrectPasswordException("Incorrect password");
        }
        if (!CONFIRM_CODE.equals(request.getConfirmCode())) {
            throw new IncorrectConfirmCodeException("Incorrect confirm code");
        }
        request.setNewPassword(passwordEncoder.encode(request.getNewPassword()));
        userService.resetPassword(request);
        return UserResponse.builder()
                .text("Password has reset successfully")
                .build();
    }

    public UserResponse logout(HttpServletRequest request) {
        var jwt = request.getHeader("Authorization").substring(7);
        blackList.addToBlackList(jwt);
        return UserResponse.builder()
                .text("Token has invalidated successfully")
                .build();
    }
}
