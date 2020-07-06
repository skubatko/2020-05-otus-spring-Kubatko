package ru.skubatko.dev.otus.spring.hw09.repository;

import ru.skubatko.dev.otus.spring.hw09.domain.Comment;

public interface CommentRepository extends CrudRepository<Comment> {

    Comment findByContent(String content);
}
