package ru.skubatko.dev.otus.spring.hw25.repository;

import ru.skubatko.dev.otus.spring.hw25.model.nosql.NoSqlBook;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoSqlBookRepository extends MongoRepository<NoSqlBook, String> {
}
