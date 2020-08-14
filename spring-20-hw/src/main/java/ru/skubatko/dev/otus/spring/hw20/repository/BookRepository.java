package ru.skubatko.dev.otus.spring.hw20.repository;

import ru.skubatko.dev.otus.spring.hw20.domain.Book;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {

    Book findByName(String name);

    List<Book> findByAuthor(String author);

    List<Book> findByGenre(String genre);
}
