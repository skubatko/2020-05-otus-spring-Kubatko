package ru.skubatko.dev.otus.spring.hw20.repository;

import ru.skubatko.dev.otus.spring.hw20.domain.Book;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {

    @Override
    Flux<Book> findAll();

    @Override
    Mono<Book> save(Book book);
}
