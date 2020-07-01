package ru.skubatko.dev.otus.spring.hw09.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import ru.skubatko.dev.otus.spring.hw09.domain.Book;
import ru.skubatko.dev.otus.spring.hw09.repository.jpa.BookRepositoryJpa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DisplayName("Jdbc dao для работы с книгами должно")
@JdbcTest
@Import(BookRepositoryJpa.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class BookDaoJdbcTest {

    @Autowired
    private BookRepositoryJpa dao;

    @DisplayName("находить ожидаемую книгу по её id")
    @Test
    void shouldFindExpectedBookById() {
        Book expected = new Book(2, "testBook2", 2, 3);
        Book actual = dao.findById(2);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("находить все книги")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldFindAllBooks() {
        List<Book> books = dao.findAll();
        assertThat(books)
                .hasSize(6)
                .extracting("name")
                .containsOnlyOnce("testBook1", "testBook2", "testBook3", "testBook4", "testBook5", "testBook6");
    }

    @DisplayName("добавлять книгу в базу данных")
    @Test
    void shouldAddBook() {
        Book expected = new Book(7, "testBook7", 2, 3);
        int result = dao.save(expected);
        assertThat(result).isEqualTo(1);

        Book actual = dao.findById(7);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("обновлять книгу в базе данных")
    @Test
    void shouldUpdateBook() {
        Book book = dao.findById(3);
        String updatedName = "testBook3Updated";
        book.setName(updatedName);
        int result = dao.update(book);
        assertThat(result).isEqualTo(1);

        Book actual = dao.findById(3);
        assertThat(actual).hasFieldOrPropertyWithValue("name", updatedName);
    }

    @DisplayName("удалять книгу по заданному id из базы данных")
    @Test
    void shouldDeleteBookById() {
        int result = dao.deleteById(1);
        assertThat(result).isEqualTo(1);

        List<Book> books = dao.findAll();
        assertThat(books).extracting("id").doesNotContain(1);
    }

    @DisplayName("возвращать ожидаемое количество книг в базе данных")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnExpectedBooksCount() {
        long actual = dao.count();
        assertThat(actual).isEqualTo(6L);
    }
}
