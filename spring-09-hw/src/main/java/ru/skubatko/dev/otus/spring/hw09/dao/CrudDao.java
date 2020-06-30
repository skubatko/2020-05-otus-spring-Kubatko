package ru.skubatko.dev.otus.spring.hw09.dao;

import java.util.List;

public interface CrudDao<T> {

    T findById(long id);

    List<T> findAll();

    int insert(T entity);

    int update(T entity);

    int deleteById(long id);

    long count();
}
