package ru.skubatko.dev.otus.spring.hw20.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import reactor.core.publisher.Mono;

public interface CommentRepository extends MongoRepository<Comment, String> {

    Mono<Comment> findByBookNameAndContent(String bookName, String content);
}
