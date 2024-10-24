package ru.gw3nax.kudagoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.gw3nax.kudagoapi.entity.Place;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("SELECT p FROM Place p LEFT JOIN FETCH p.events WHERE p.id = :id")
    Optional<Place> findByIdWithQuery(@Param("id") Long id);
}
