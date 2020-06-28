package ru.skubatko.dev.otus.spring.hw07.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Author {
    private long id;
    private String name;
}
