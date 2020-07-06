package ru.skubatko.dev.otus.spring.hw09.service;

import static org.assertj.core.api.Assertions.assertThat;

import ru.skubatko.dev.otus.spring.hw09.domain.Genre;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DisplayName("Сервис для работы с жанрами должен")
@Transactional
@SpringBootTest
class GenreServiceTest {

    @Autowired
    private GenreService service;

    @DisplayName("находить ожидаемый жанр по его id")
    @Test
    void shouldFindExpectedGenreById() {
        Genre expected = new Genre(2, "testGenre2");
        Genre actual = service.findById(2);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("находить ожидаемый жанр по его имени")
    @Test
    void shouldFindExpectedGenreByName() {
        String name = "testGenre2";
        Genre expected = new Genre(2, name);
        Genre actual = service.findByName(name);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("находить все жанры")
    @Test
    void shouldFindAllGenres() {
        List<Genre> genres = service.findAll();
        assertThat(genres)
                .hasSize(4)
                .extracting("name").containsOnlyOnce("testGenre1", "testGenre2", "testGenre3", "testGenre4");
    }

    @DisplayName("добавлять жанр")
    @Test
    void shouldAddGenre() {
        Genre expected = new Genre();
        String name = "testGenreNew";
        expected.setName(name);
        service.save(expected);

        Genre actual = service.findByName(name);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("обновлять жанр")
    @Test
    void shouldUpdateGenre() {
        Genre genre = service.findById(3);
        String updatedName = "testGenre3Updated";
        genre.setName(updatedName);
        service.update(genre);

        Genre actual = service.findById(3);
        assertThat(actual).hasFieldOrPropertyWithValue("name", updatedName);
    }

    @DisplayName("удалять жанр по заданному id")
    @Test
    void shouldDeleteGenreById() {
        service.deleteById(1);

        List<Genre> genres = service.findAll();
        assertThat(genres).extracting("id").doesNotContain(1);
    }
}
