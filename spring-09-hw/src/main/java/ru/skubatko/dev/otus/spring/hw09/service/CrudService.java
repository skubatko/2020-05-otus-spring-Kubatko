package ru.skubatko.dev.otus.spring.hw09.service;

import java.util.List;

public interface CrudService<T> {

    T findById(long id);

    List<T> findAll();

    void save(T entity);

    void update(T entity);

    void deleteById(long id);
}
