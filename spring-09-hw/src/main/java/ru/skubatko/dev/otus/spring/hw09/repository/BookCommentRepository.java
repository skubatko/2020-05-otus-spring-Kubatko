package ru.skubatko.dev.otus.spring.hw09.repository;

import ru.skubatko.dev.otus.spring.hw09.domain.BookComment;

import java.util.List;

public interface BookCommentRepository extends CrudRepository<BookComment> {

    BookComment findByContent(String content);

    List<BookComment> findAllByBookId(long bookId);
}
