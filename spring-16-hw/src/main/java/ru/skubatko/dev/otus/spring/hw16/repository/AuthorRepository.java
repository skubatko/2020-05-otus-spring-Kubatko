package ru.skubatko.dev.otus.spring.hw16.repository;

import ru.skubatko.dev.otus.spring.hw16.domain.Author;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Author findByName(String name);
}
