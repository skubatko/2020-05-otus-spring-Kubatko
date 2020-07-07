package ru.skubatko.dev.otus.spring.hw11.repository;

import ru.skubatko.dev.otus.spring.hw11.domain.Comment;

public interface CommentRepository extends CrudRepository<Comment> {

    Comment findByContent(String content);
}
