package ru.skubatko.dev.otus.spring.hw17.repository;

import ru.skubatko.dev.otus.spring.hw17.domain.Genre;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Genre findByName(String name);
}
