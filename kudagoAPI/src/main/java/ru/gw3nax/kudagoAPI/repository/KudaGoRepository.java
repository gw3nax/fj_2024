package ru.gw3nax.kudagoAPI.repository;

import org.springframework.stereotype.Component;
import ru.gw3nax.kudagoAPI.entity.Category;

import java.util.List;
import java.util.Optional;

@Component
public interface KudaGoRepository<T> {
    List<T> findAll();

    Optional<T> findById(Long id);

    void save(T entity);

    void update(Long id, T entity);

    void deleteById(Long id);

    void clearAll();
}