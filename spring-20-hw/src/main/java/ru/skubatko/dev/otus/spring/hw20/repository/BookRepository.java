package ru.skubatko.dev.otus.spring.hw20.repository;

import ru.skubatko.dev.otus.spring.hw20.domain.Book;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {

    Mono<Book> findByName(String name);

    Mono<Void> deleteByName(String name);
}
