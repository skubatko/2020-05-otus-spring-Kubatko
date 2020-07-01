package ru.skubatko.dev.otus.spring.hw09.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ru.skubatko.dev.otus.spring.hw09.domain.Author;
import ru.skubatko.dev.otus.spring.hw09.repository.jpa.AuthorRepositoryJpa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@DisplayName("Репозиторий для работы с авторами должен")
@DataJpaTest
@Import(AuthorRepositoryJpa.class)
class AuthorRepositoryJpaTest {

    @Autowired
    private AuthorRepositoryJpa repository;

    @DisplayName("находить ожидаемого автора по его id")
    @Test
    void shouldFindExpectedAuthorById() {
        Author expected = new Author(2, "testAuthor2");
        Author actual = repository.findById(2).orElse(null);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("находить ожидаемого автора по его имени")
    @Test
    void shouldFindExpectedAuthorByName() {
        String name = "testAuthor2";
        Author expected = new Author(2, name);
        Author actual = repository.findByName(name).orElse(null);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("находить всех авторов")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldFindAllAuthors() {
        List<Author> authors = repository.findAll();
        assertThat(authors)
                .hasSize(3)
                .extracting("name").containsOnlyOnce("testAuthor1", "testAuthor2", "testAuthor3");
    }

    @DisplayName("добавлять автора в базу данных")
    @Test
    void shouldAddAuthor() {
        Author expected = new Author(4, "testAuthor4");
        repository.save(expected);

        Author actual = repository.findById(4).orElse(null);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("обновлять автора в базе данных")
    @Test
    void shouldUpdateAuthor() {
        Author author = repository.findById(3).orElse(null);
        String updatedName = "testAuthor3Updated";
        author.setName(updatedName);
        repository.update(author);

        Author actual = repository.findById(3).orElse(null);
        assertThat(actual).hasFieldOrPropertyWithValue("name", updatedName);
    }

    @DisplayName("удалять автора по заданному id из базы данных")
    @Test
    void shouldDeleteAuthorById() {
        repository.deleteById(1);

        List<Author> authors = repository.findAll();
        assertThat(authors).extracting("id").doesNotContain(1);
    }

    @DisplayName("возвращать ожидаемое количество авторов в базе данных")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnExpectedAuthorsCount() {
        long actual = repository.count();
        assertThat(actual).isEqualTo(3L);
    }
}
