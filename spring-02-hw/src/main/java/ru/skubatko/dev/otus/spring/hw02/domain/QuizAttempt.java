package ru.skubatko.dev.otus.spring.hw02.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class QuizAttempt {
    private String participantName;
    private Map<Question, Answer> content = new HashMap<>();
}
