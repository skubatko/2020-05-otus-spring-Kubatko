package ru.skubatko.dev.otus.spring.hw16.repository;

import ru.skubatko.dev.otus.spring.hw16.domain.Genre;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Genre findByName(String name);
}
