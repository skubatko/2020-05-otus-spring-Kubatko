package ru.skubatko.dev.otus.spring.hw09.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {

    Optional<T> findById(long id);

    List<T> findAll();

    T save(T entity);

    void update(T entity);

    void deleteById(long id);

    long count();
}
