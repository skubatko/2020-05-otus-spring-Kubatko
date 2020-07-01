package ru.skubatko.dev.otus.spring.hw09.repository;

import ru.skubatko.dev.otus.spring.hw09.domain.Book;

import java.util.Optional;

public interface BookRepository extends CrudRepository<Book> {

    Optional<Book> findByName(String name);
}
