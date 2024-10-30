package ru.gw3nax.kudagoapi.memento.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@MappedSuperclass
public class Snapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate createdAt;

    @PrePersist
    public void init() {
        createdAt = LocalDate.now();
    }
}
