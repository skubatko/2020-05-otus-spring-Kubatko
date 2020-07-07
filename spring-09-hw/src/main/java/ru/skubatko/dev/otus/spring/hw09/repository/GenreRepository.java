package ru.skubatko.dev.otus.spring.hw09.repository;

import ru.skubatko.dev.otus.spring.hw09.domain.Genre;

public interface GenreRepository extends CrudRepository<Genre> {

    Genre findByName(String name);
}
