package ru.skubatko.dev.otus.spring.hw29.repository;

import ru.skubatko.dev.otus.spring.hw29.domain.Genre;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Genre findByName(String name);
}
