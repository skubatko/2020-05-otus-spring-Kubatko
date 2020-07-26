package ru.skubatko.dev.otus.spring.hw16.repository;

import ru.skubatko.dev.otus.spring.hw16.domain.Author;
import ru.skubatko.dev.otus.spring.hw16.domain.Book;
import ru.skubatko.dev.otus.spring.hw16.domain.Genre;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = {"author", "genre", "comments"})
    @Override
    List<Book> findAll();

    Book findByName(String name);

    List<Book> findByAuthor(Author author);

    List<Book> findByGenre(Genre genre);
}
