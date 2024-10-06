package ru.gw3nax.kudagoAPI.repository;

import org.springframework.stereotype.Repository;
import ru.gw3nax.kudagoAPI.entity.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CategoryRepository implements KudaGoRepository<Category> {
    private final ConcurrentHashMap<Long, Category> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void save(Category category) {
        Long id = idGenerator.getAndIncrement();
        category.setId(id);
        storage.put(id, category);
    }
    @Override
    public void update(Long id, Category category) {
        storage.put(id, category);
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
}
