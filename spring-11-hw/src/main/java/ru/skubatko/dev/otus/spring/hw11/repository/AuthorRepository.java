package ru.skubatko.dev.otus.spring.hw11.repository;

import ru.skubatko.dev.otus.spring.hw11.domain.Author;

public interface AuthorRepository extends CrudRepository<Author> {

    Author findByName(String name);
}
