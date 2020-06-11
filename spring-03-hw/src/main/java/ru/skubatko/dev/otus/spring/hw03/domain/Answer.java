package ru.skubatko.dev.otus.spring.hw03.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Answer {
    private String content;
    private boolean isCorrect;
}
