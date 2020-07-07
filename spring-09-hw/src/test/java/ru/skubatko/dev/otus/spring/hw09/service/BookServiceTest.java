package ru.skubatko.dev.otus.spring.hw09.service;

import static org.assertj.core.api.Assertions.assertThat;

import ru.skubatko.dev.otus.spring.hw09.domain.Author;
import ru.skubatko.dev.otus.spring.hw09.domain.Book;
import ru.skubatko.dev.otus.spring.hw09.domain.Comment;
import ru.skubatko.dev.otus.spring.hw09.domain.Genre;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@DisplayName("Сервис для работы с книгами должен")
@Transactional
@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookService service;

    @DisplayName("находить ожидаемую книгу с комментариями по её id")
    @Test
    void shouldFindExpectedBookById() {
        Author author = new Author(2, "testAuthor2");
        Genre genre = new Genre(3, "testGenre3");
        Book expected = new Book(2, "testBook2", author, genre, null);

        Book actual = service.findById(2);

        assertThat(actual).isEqualToIgnoringGivenFields(expected, "comments");
        assertThat(actual.getComments()).hasSize(1);
        assertThat(actual.getComments().get(0).getContent()).isEqualTo("testBookComment2");
    }

    @DisplayName("находить ожидаемую книгу с комментариями по её имени")
    @Test
    void shouldFindExpectedBookByNameWithComments() {
        Author author = new Author(2, "testAuthor2");
        Genre genre = new Genre(3, "testGenre3");
        String name = "testBook2";
        Book expected = new Book(2, name, author, genre, null);

        Book actual = service.findByName(name);

        assertThat(actual).isEqualToIgnoringGivenFields(expected, "comments");
        assertThat(actual.getComments()).hasSize(1);
        assertThat(actual.getComments().get(0).getContent()).isEqualTo("testBookComment2");
    }

    @DisplayName("находить все книги с комментариями")
    @Test
    void shouldFindAllBooksWithComments() {
        List<Book> books = service.findAll();

        assertThat(books)
                .hasSize(6)
                .extracting("name")
                .containsOnlyOnce("testBook1", "testBook2", "testBook3", "testBook4", "testBook5", "testBook6");

        List<Comment> comments =
                books.stream().flatMap(book -> book.getComments().stream()).collect(Collectors.toList());
        assertThat(comments)
                .hasSize(6)
                .extracting("content")
                .containsOnlyOnce("testBookComment1", "testBookComment2", "testBookComment3",
                        "testBookComment4", "testBookComment5", "testBookComment6");
    }

    @DisplayName("добавлять книгу")
    @Test
    void shouldAddBook() {
        Author author = new Author();
        author.setName("newAuthor");

        Genre genre = new Genre();
        genre.setName("newGenre");

        Book book = new Book();
        String bookName = "testBook7";
        book.setName(bookName);
        book.setAuthor(author);
        book.setGenre(genre);

        service.save(book);

        Book actual = service.findByName(bookName);

        assertThat(actual).isEqualTo(book);
    }

    @DisplayName("обновлять книгу")
    @Test
    void shouldUpdateBook() {
        Book book = service.findById(3);
        String updatedName = "testBook3Updated";
        book.setName(updatedName);
        service.update(book);

        Book actual = service.findById(3);
        assertThat(actual).hasFieldOrPropertyWithValue("name", updatedName);
    }

    @DisplayName("удалять книгу по заданному id")
    @Test
    void shouldDeleteBookById() {
        service.deleteById(1L);

        List<Book> books = service.findAll();
        assertThat(books).extracting("id").doesNotContain(1L);
    }
}
