package ru.skubatko.dev.otus.spring.hw11.repository;

import ru.skubatko.dev.otus.spring.hw11.domain.Book;

public interface BookRepository extends CrudRepository<Book> {

    Book findByName(String name);
}
