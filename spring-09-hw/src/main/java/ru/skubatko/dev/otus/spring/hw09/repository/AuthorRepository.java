package ru.skubatko.dev.otus.spring.hw09.repository;

import ru.skubatko.dev.otus.spring.hw09.domain.Author;

import java.util.Optional;

public interface AuthorRepository extends CrudRepository<Author> {

    Optional<Author> findByName(String name);
}
