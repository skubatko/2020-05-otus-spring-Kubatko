package ru.skubatko.dev.otus.spring.hw09.service;

import java.util.List;

public interface CrudService<T> {

    T findById(long id);

    List<T> findAll();

    int insert(T entity);

    int update(T entity);

    int deleteById(long id);

    long count();
}
