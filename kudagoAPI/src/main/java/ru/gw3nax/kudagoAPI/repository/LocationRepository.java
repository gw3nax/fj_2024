package ru.gw3nax.kudagoAPI.repository;

import org.springframework.stereotype.Repository;
import ru.gw3nax.kudagoAPI.entity.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class LocationRepository implements KudaGoRepository<Location> {
    private final ConcurrentHashMap<Long, Location> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public List<Location> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Location> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void save(Location location) {
        Long id = idGenerator.incrementAndGet();
        location.setId(id);
        storage.put(id, location);
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }

    @Override
    public void clearAll() {
        storage.clear();
        idGenerator.set(0);
    }

    @Override
    public void update(Long id, Location location) {
        storage.put(id, location);
    }
}
