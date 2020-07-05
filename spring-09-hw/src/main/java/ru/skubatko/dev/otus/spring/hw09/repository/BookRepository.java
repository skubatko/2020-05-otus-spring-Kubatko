package ru.skubatko.dev.otus.spring.hw09.repository;

import ru.skubatko.dev.otus.spring.hw09.domain.Book;

import java.util.List;

public interface BookRepository extends CrudRepository<Book> {

    Book findByIdWithComments(long id);

    Book findByName(String name);

    Book findByNameWithComments(String name);

    public List<Book> findAllWithComments();
}
