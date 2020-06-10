package ru.skubatko.dev.otus.spring.hw02.service;

import ru.skubatko.dev.otus.spring.hw02.domain.Quiz;
import ru.skubatko.dev.otus.spring.hw02.domain.QuizAttempt;
import ru.skubatko.dev.otus.spring.hw02.enums.Mark;

public interface QuizService {

    Quiz getQuiz();

    Mark getQuizAttemptMark(QuizAttempt quizAttempt);
}
