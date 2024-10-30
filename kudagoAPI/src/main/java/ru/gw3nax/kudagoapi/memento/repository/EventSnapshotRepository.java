package ru.gw3nax.kudagoapi.memento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gw3nax.kudagoapi.memento.entity.EventSnapshot;

@Repository
public interface EventSnapshotRepository extends JpaRepository<EventSnapshot, Long> {
}
