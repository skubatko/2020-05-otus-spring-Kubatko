package ru.skubatko.dev.otus.spring.hw20.repository;

import ru.skubatko.dev.otus.spring.hw20.domain.Comment;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {

    Comment findByBookNameAndContent(String bookName, String content);
}
