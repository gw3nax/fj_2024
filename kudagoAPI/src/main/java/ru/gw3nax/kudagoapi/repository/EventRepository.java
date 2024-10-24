package ru.gw3nax.kudagoapi.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gw3nax.kudagoapi.entity.Event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    static Specification<Event> buildSpecification(String name, Long place, String fromDate, String toDate) {
        var specifications = new ArrayList<Specification<Event>>();
        if (name != null) {
            specifications.add((Specification<Event>) (event, query, cb) ->
                    cb.equal(event.get("name"), name));
        }
        if (place != null) {
            specifications.add((Specification<Event>) (event, query, cb) ->
                    cb.equal(event.get("place").get("id"), place));
        }
        if (fromDate != null && !fromDate.isEmpty()) {
            specifications.add((Specification<Event>) (event, query, cb) ->
                    cb.greaterThanOrEqualTo(event.get("date").as(LocalDate.class), LocalDate.parse(fromDate)));
        }
        if (toDate != null && !toDate.isEmpty()) {
            specifications.add((Specification<Event>) (event, query, cb) ->
                    cb.lessThanOrEqualTo(event.get("date").as(LocalDate.class), LocalDate.parse(toDate)));
        }
        return specifications.stream().reduce(Specification::and).orElse(null);
    }

    List<Event> findAll(Specification<Event> specification);
}
