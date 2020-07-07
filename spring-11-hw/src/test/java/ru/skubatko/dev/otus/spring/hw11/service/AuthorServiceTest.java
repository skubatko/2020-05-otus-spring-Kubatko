package ru.skubatko.dev.otus.spring.hw11.service;

import static org.assertj.core.api.Assertions.assertThat;

import ru.skubatko.dev.otus.spring.hw11.domain.Author;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DisplayName("Сервис для работы с авторами должен")
@Transactional
@SpringBootTest
class AuthorServiceTest {

    @Autowired
    private AuthorService service;

    @DisplayName("находить ожидаемого автора по его id")
    @Test
    void shouldFindExpectedAuthorById() {
        Author expected = new Author(2, "testAuthor2");
        Author actual = service.findById(2);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("находить ожидаемого автора по его имени")
    @Test
    void shouldFindExpectedAuthorByName() {
        String name = "testAuthor2";
        Author expected = new Author(2, name);
        Author actual = service.findByName(name);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("находить всех авторов")
    @Test
    void shouldFindAllAuthors() {
        List<Author> authors = service.findAll();
        assertThat(authors)
                .hasSize(3)
                .extracting("name").containsOnlyOnce("testAuthor1", "testAuthor2", "testAuthor3");
    }

    @DisplayName("добавлять автора")
    @Test
    void shouldAddAuthor() {
        Author author = new Author();
        String name = "testAuthor4";
        author.setName(name);

        service.save(author);

        Author actual = service.findByName(name);

        assertThat(actual).isEqualTo(author);
    }

    @DisplayName("обновлять автора")
    @Test
    void shouldUpdateAuthor() {
        Author author = service.findById(3);
        String updatedName = "testAuthor3Updated";
        author.setName(updatedName);
        service.update(author);

        Author actual = service.findById(3);
        assertThat(actual).hasFieldOrPropertyWithValue("name", updatedName);
    }

    @DisplayName("удалять автора по заданному id")
    @Test
    void shouldDeleteAuthorById() {
        service.deleteById(1);

        List<Author> authors = service.findAll();
        assertThat(authors).extracting("id").doesNotContain(1);
    }
}
