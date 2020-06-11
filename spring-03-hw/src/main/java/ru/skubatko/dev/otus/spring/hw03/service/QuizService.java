package ru.skubatko.dev.otus.spring.hw03.service;

import ru.skubatko.dev.otus.spring.hw03.domain.Quiz;
import ru.skubatko.dev.otus.spring.hw03.domain.QuizAttempt;
import ru.skubatko.dev.otus.spring.hw03.enums.Mark;

public interface QuizService {

    Quiz getQuiz();

    Mark getQuizAttemptMark(QuizAttempt quizAttempt);
}
