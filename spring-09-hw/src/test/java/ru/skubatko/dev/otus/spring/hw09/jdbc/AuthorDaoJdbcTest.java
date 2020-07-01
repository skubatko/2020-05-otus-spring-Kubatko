package ru.skubatko.dev.otus.spring.hw09.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import ru.skubatko.dev.otus.spring.hw09.domain.Author;
import ru.skubatko.dev.otus.spring.hw09.repository.jpa.AuthorRepositoryJpa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DisplayName("Jdbc dao для работы с авторами должно")
@JdbcTest
@Import(AuthorRepositoryJpa.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class AuthorDaoJdbcTest {

    @Autowired
    private AuthorRepositoryJpa dao;

    @DisplayName("находить ожидаемого автора по его id")
    @Test
    void shouldFindExpectedAuthorById() {
        Author expected = new Author(2, "testAuthor2");
        Author actual = dao.findById(2);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("находить всех авторов")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldFindAllAuthors() {
        List<Author> authors = dao.findAll();
        assertThat(authors)
                .hasSize(3)
                .extracting("name").containsOnlyOnce("testAuthor1", "testAuthor2", "testAuthor3");
    }

    @DisplayName("добавлять автора в базу данных")
    @Test
    void shouldAddAuthor() {
        Author expected = new Author(4, "testAuthor4");
        int result = dao.save(expected);
        assertThat(result).isEqualTo(1);

        Author actual = dao.findById(4);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("обновлять автора в базе данных")
    @Test
    void shouldUpdateAuthor() {
        Author author = dao.findById(3);
        String updatedName = "testAuthor3Updated";
        author.setName(updatedName);
        int result = dao.update(author);
        assertThat(result).isEqualTo(1);

        Author actual = dao.findById(3);
        assertThat(actual).hasFieldOrPropertyWithValue("name", updatedName);
    }

    @DisplayName("удалять автора по заданному id из базы данных")
    @Test
    void shouldDeleteAuthorById() {
        int result = dao.deleteById(1);
        assertThat(result).isEqualTo(1);

        List<Author> authors = dao.findAll();
        assertThat(authors).extracting("id").doesNotContain(1);
    }

    @DisplayName("возвращать ожидаемое количество авторов в базе данных")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnExpectedAuthorsCount() {
        long actual = dao.count();
        assertThat(actual).isEqualTo(3L);
    }
}
