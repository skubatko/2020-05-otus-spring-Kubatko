package ru.skubatko.dev.otus.spring.hw09.repository;

import ru.skubatko.dev.otus.spring.hw09.domain.Author;

public interface AuthorRepository extends CrudRepository<Author> {

    Author findByName(String name);
}
