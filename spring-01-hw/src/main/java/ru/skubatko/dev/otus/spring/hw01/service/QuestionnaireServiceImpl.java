package ru.skubatko.dev.otus.spring.hw01.service;

import ru.skubatko.dev.otus.spring.hw01.dao.QuestionnaireDao;
import ru.skubatko.dev.otus.spring.hw01.domain.Questionnaire;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QuestionnaireServiceImpl implements QuestionnaireService {

    private final QuestionnaireDao dao;

    @Override
    public void printOut() {
        Questionnaire questionnaire = dao.getOne();
        questionnaire.getQuestions().forEach(System.out::println);
    }
}
