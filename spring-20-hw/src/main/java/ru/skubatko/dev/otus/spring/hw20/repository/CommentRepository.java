package ru.skubatko.dev.otus.spring.hw20.repository;

import ru.skubatko.dev.otus.spring.hw20.domain.Comment;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {

    Mono<Void> deleteAllByBookName(String bookName);
}
