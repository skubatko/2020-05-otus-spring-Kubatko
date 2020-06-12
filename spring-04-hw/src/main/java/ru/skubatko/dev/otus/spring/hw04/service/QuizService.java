package ru.skubatko.dev.otus.spring.hw04.service;

import ru.skubatko.dev.otus.spring.hw04.domain.Quiz;
import ru.skubatko.dev.otus.spring.hw04.domain.QuizAttempt;
import ru.skubatko.dev.otus.spring.hw04.enums.Mark;

public interface QuizService {

    Quiz getQuiz();

    Mark getQuizAttemptMark(QuizAttempt quizAttempt);
}
