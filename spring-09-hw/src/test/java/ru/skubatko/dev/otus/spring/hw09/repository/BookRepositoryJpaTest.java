package ru.skubatko.dev.otus.spring.hw09.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ru.skubatko.dev.otus.spring.hw09.domain.Author;
import ru.skubatko.dev.otus.spring.hw09.domain.Book;
import ru.skubatko.dev.otus.spring.hw09.domain.BookComment;
import ru.skubatko.dev.otus.spring.hw09.domain.Genre;
import ru.skubatko.dev.otus.spring.hw09.repository.jpa.BookRepositoryJpa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collections;
import java.util.List;

@DisplayName("Репозиторий для работы с книгами должен")
@DataJpaTest
@Import(BookRepositoryJpa.class)
class BookRepositoryJpaTest {

    @Autowired
    private BookRepositoryJpa repository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("находить ожидаемую книгу по её id")
    @Test
    void shouldFindExpectedBookById() {
        Author author = new Author(2, "testAuthor2");
        Genre genre = new Genre(3, "testGenre3");
        Book expected = new Book(2, "testBook2", author, genre, Collections.emptyList());

        Book actual = repository.findById(2).orElse(null);

        assertThat(actual).isEqualToIgnoringGivenFields(expected, "bookComments");
        assertThat(actual.getBookComments()).hasSize(1);
        assertThat(actual.getBookComments().get(0)).hasFieldOrPropertyWithValue("content", "testBookComment2");
    }

    @DisplayName("находить ожидаемую книгу по её имени")
    @Test
    void shouldFindExpectedBookByName() {
        Author author = new Author(2, "testAuthor2");
        Genre genre = new Genre(3, "testGenre3");
        String name = "testBook2";
        Book expected = new Book(2, name, author, genre, Collections.emptyList());
        Book actual = repository.findByName(name);
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "bookComments");
        assertThat(actual.getBookComments()).hasSize(1);
        assertThat(actual.getBookComments().get(0)).hasFieldOrPropertyWithValue("content", "testBookComment2");
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
        Book book = new Book();

        String name = "testBook7";
        book.setName(name);

        book.setAuthor(em.find(Author.class, 2L));
        book.setGenre(em.find(Genre.class, 3L));

        repository.save(book);

        Book actual = repository.findByName(name);
        assertThat(actual).isEqualTo(book);
    }

    @DisplayName("обновлять книгу в базе данных")
    @Test
    void shouldUpdateBook() {
        Book book = repository.findByName("testBook3");
        String updatedName = "testBook3Updated";
        book.setName(updatedName);
        repository.save(book);

        Book actual = repository.findById(3).orElse(null);
        assertThat(actual).hasFieldOrPropertyWithValue("name", updatedName);
    }

    @DisplayName("удалять книгу по заданному id из базы данных")
    @Test
    void shouldDeleteBookById() {
        repository.deleteById(1L);

        List<Book> books = repository.findAll();
        assertThat(books).extracting("id").doesNotContain(1L);
        assertThat(em.find(BookComment.class, 1L)).isNull();
        assertThat(em.find(BookComment.class, 2L)).isNull();
        assertThat(em.find(BookComment.class, 3L)).isNull();
    }

    @DisplayName("возвращать ожидаемое количество книг в базе данных")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnExpectedBooksCount() {
        long actual = repository.count();
        assertThat(actual).isEqualTo(6L);
    }
}
