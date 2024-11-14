package ru.gw3nax.kudagoapi.security;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class BlackList {

    private final Set<String> tokenBlackList = new HashSet<>();

    public boolean isBlackListed(String token) {
        return tokenBlackList.contains(token);
    }

    public void addToBlackList(String token) {
        tokenBlackList.add(token);
    }
}