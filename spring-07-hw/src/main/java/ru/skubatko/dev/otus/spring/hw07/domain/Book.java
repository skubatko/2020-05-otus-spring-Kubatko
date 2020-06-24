package ru.skubatko.dev.otus.spring.hw07.domain;

import lombok.Data;

@Data
public class Book {
    private long id;
    private String name;
    private long authorId;
    private long genreId;
}
