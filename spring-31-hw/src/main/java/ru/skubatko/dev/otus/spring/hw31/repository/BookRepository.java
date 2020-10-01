package ru.skubatko.dev.otus.spring.hw31.repository;

import ru.skubatko.dev.otus.spring.hw31.domain.Author;
import ru.skubatko.dev.otus.spring.hw31.domain.Book;
import ru.skubatko.dev.otus.spring.hw31.domain.Genre;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "books", path = "books")
public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = {"author", "genre", "comments"})
    @Override
    List<Book> findAll();

    Book findByName(String name);

    List<Book> findByAuthor(Author author);

    List<Book> findByGenre(Genre genre);
}
