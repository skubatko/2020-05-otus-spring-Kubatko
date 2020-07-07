package ru.skubatko.dev.otus.spring.hw11.repository;

import ru.skubatko.dev.otus.spring.hw11.domain.Genre;

public interface GenreRepository extends CrudRepository<Genre> {

    Genre findByName(String name);
}
