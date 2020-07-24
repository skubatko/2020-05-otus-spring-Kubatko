package ru.skubatko.dev.otus.spring.hw16.repository;

import ru.skubatko.dev.otus.spring.hw16.domain.Book;
import ru.skubatko.dev.otus.spring.hw16.domain.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findByBookAndContent(Book book, String content);
}
