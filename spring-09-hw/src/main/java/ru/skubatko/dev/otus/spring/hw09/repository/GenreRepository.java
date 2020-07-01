package ru.skubatko.dev.otus.spring.hw09.repository;

import ru.skubatko.dev.otus.spring.hw09.domain.Genre;

import java.util.Optional;

public interface GenreRepository extends CrudRepository<Genre> {

    Optional<Genre> findByName(String name);
}
