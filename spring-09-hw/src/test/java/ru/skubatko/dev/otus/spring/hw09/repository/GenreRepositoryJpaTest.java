package ru.skubatko.dev.otus.spring.hw09.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ru.skubatko.dev.otus.spring.hw09.domain.Genre;
import ru.skubatko.dev.otus.spring.hw09.repository.jpa.GenreRepositoryJpa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@DisplayName("Репозиторий для работы с жанрами должен")
@DataJpaTest
@Import(GenreRepositoryJpa.class)
class GenreRepositoryJpaTest {

    @Autowired
    private GenreRepositoryJpa repository;

    @DisplayName("находить ожидаемый жанр по его id")
    @Test
    void shouldFindExpectedGenreById() {
        Genre expected = new Genre(2, "testGenre2");
        Genre actual = repository.findById(2).orElse(null);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("находить ожидаемый жанр по его имени")
    @Test
    void shouldFindExpectedGenreByName() {
        String name = "testGenre2";
        Genre expected = new Genre(2, name);
        Genre actual = repository.findByName(name).orElse(null);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("находить все жанры")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldFindAllGenres() {
        List<Genre> genres = repository.findAll();
        assertThat(genres)
                .hasSize(4)
                .extracting("name").containsOnlyOnce("testGenre1", "testGenre2", "testGenre3", "testGenre4");
    }

    @DisplayName("добавлять жанр в базу данных")
    @Test
    void shouldAddGenre() {
        Genre expected = new Genre();
        String name = "testGenre5";
        expected.setName(name);
        repository.save(expected);

        Genre actual = repository.findByName(name).orElse(null);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("обновлять жанр в базе данных")
    @Test
    void shouldUpdateGenre() {
        Genre initialGenre = repository.findById(3).orElse(null);
        String updatedName = "testGenre3Updated";
        initialGenre.setName(updatedName);
        repository.update(initialGenre);

        Genre updatedGenre = repository.findById(3).orElse(null);
        assertThat(updatedGenre).hasFieldOrPropertyWithValue("name", updatedName);
    }

    @DisplayName("удалять жанр по заданному id из базы данных")
    @Test
    void shouldDeleteGenreById() {
        repository.deleteById(1);

        List<Genre> genres = repository.findAll();
        assertThat(genres).extracting("id").doesNotContain(1);
    }

    @DisplayName("возвращать ожидаемое количество жанров в базе данных")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnExpectedGenresCount() {
        long actual = repository.count();
        assertThat(actual).isEqualTo(4L);
    }
}
