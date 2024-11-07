package ru.gw3nax.kudagoapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.gw3nax.kudagoapi.controller.dto.ResetPasswordRequest;
import ru.gw3nax.kudagoapi.entity.UserEntity;
import ru.gw3nax.kudagoapi.exception.UserAlreadyExistException;
import ru.gw3nax.kudagoapi.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public UserEntity save(UserEntity user) {
        return repository.save(user);
    }

    public UserEntity create(UserEntity user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistException("Пользователь с таким именем уже существует");
        }

        return save(user);
    }

    public UserEntity getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public void resetPassword(ResetPasswordRequest request) {
        var user = getByUsername(request.getUsername());
        user.setPassword(request.getNewPassword());
        repository.save(user);
    }
}
