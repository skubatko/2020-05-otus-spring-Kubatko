package ru.skubatko.dev.otus.spring.hw09.repository;

import ru.skubatko.dev.otus.spring.hw09.domain.BookComment;

import java.util.Optional;

public interface BookCommentRepository extends CrudRepository<BookComment> {

    Optional<BookComment> findByContent(String content);
}
