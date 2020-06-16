package ru.skubatko.dev.otus.spring.hw05.service;

import ru.skubatko.dev.otus.spring.hw05.domain.Quiz;
import ru.skubatko.dev.otus.spring.hw05.domain.QuizAttempt;
import ru.skubatko.dev.otus.spring.hw05.enums.Mark;

public interface QuizService {

    Quiz getQuiz();

    Mark getQuizAttemptMark(QuizAttempt quizAttempt);
}
