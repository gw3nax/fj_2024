package ru.gw3nax.kudagoapi.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secured")
public class SecuredController {

    @GetMapping
    public ResponseEntity<String> secured() {
        return ResponseEntity.ok("Secured");
    }
}
