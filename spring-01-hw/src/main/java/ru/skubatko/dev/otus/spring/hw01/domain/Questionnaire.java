package ru.skubatko.dev.otus.spring.hw01.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Questionnaire {
    private List<String> questions = new ArrayList<>();
}
