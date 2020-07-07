package ru.skubatko.dev.otus.spring.hw11.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {

    Optional<T> findById(long id);

    List<T> findAll();

    T save(T entity);

    void deleteById(long id);
}
