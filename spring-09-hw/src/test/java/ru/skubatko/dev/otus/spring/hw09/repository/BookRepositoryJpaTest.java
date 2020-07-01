package ru.skubatko.dev.otus.spring.hw09.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ru.skubatko.dev.otus.spring.hw09.domain.Author;
import ru.skubatko.dev.otus.spring.hw09.domain.Book;
import ru.skubatko.dev.otus.spring.hw09.domain.Genre;
import ru.skubatko.dev.otus.spring.hw09.repository.jpa.BookRepositoryJpa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@DisplayName("Репозиторий для работы с книгами должен")
@JdbcTest
@Import(BookRepositoryJpa.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class BookRepositoryJpaTest {

    @Autowired
    private BookRepositoryJpa repository;

    @DisplayName("находить ожидаемую книгу по её id")
    @Test
    void shouldFindExpectedBookById() {
        Author author = new Author(2, "testAuthor2");
        Genre genre = new Genre(3, "testGenre3");
        Book expected = new Book(2, "testBook2", author, genre, Collections.emptyList());
        Book actual = repository.findById(2).orElse(null);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("находить все книги")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldFindAllBooks() {
        List<Book> books = repository.findAll();
        assertThat(books)
                .hasSize(6)
                .extracting("name")
                .containsOnlyOnce("testBook1", "testBook2", "testBook3", "testBook4", "testBook5", "testBook6");
    }

    @DisplayName("добавлять книгу в базу данных")
    @Test
    void shouldAddBook() {
        Author author = new Author(2, "testAuthor2");
        Genre genre = new Genre(3, "testGenre3");
        Book expected = new Book(7, "testBook7", author, genre, Collections.emptyList());
        repository.save(expected);

        Book actual = repository.findById(7).orElse(null);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("обновлять книгу в базе данных")
    @Test
    void shouldUpdateBook() {
        Book book = repository.findById(3).orElse(null);
        String updatedName = "testBook3Updated";
        book.setName(updatedName);
        repository.update(book);

        Book actual = repository.findById(3).orElse(null);
        assertThat(actual).hasFieldOrPropertyWithValue("name", updatedName);
    }

    @DisplayName("удалять книгу по заданному id из базы данных")
    @Test
    void shouldDeleteBookById() {
        repository.deleteById(1);

        List<Book> books = repository.findAll();
        assertThat(books).extracting("id").doesNotContain(1);
    }

    @DisplayName("возвращать ожидаемое количество книг в базе данных")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnExpectedBooksCount() {
        long actual = repository.count();
        assertThat(actual).isEqualTo(6L);
    }
}
