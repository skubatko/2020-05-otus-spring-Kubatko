package ru.skubatko.dev.otus.spring.hw25.repository.nosql;

import ru.skubatko.dev.otus.spring.hw25.model.nosql.NoSqlBook;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NoSqlBookRepository extends MongoRepository<NoSqlBook, String> {

    NoSqlBook findByName(String name);

    List<NoSqlBook> findByAuthor(String author);

    List<NoSqlBook> findByGenre(String genre);
}
