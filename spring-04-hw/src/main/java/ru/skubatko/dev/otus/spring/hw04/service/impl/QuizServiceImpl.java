package ru.skubatko.dev.otus.spring.hw04.service.impl;

import ru.skubatko.dev.otus.spring.hw04.dao.QuizDao;
import ru.skubatko.dev.otus.spring.hw04.domain.Answer;
import ru.skubatko.dev.otus.spring.hw04.domain.Question;
import ru.skubatko.dev.otus.spring.hw04.domain.Quiz;
import ru.skubatko.dev.otus.spring.hw04.domain.QuizAttempt;
import ru.skubatko.dev.otus.spring.hw04.enums.Mark;
import ru.skubatko.dev.otus.spring.hw04.service.QuizService;

import com.google.common.collect.Multimap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizDao dao;

    @Override
    public Quiz getQuiz() {
        return dao.get();
    }

    @Override
    public Mark getQuizAttemptMark(QuizAttempt quizAttempt) {
        Map<Question, Answer> attemptContent = quizAttempt.getContent();

        int numberOfRightAnswers = 0;
        for (Map.Entry<Question, Answer> entry : attemptContent.entrySet()) {
            Question question = entry.getKey();
            Answer answer = entry.getValue();

            if (isAnswerRight(question, answer)) {
                numberOfRightAnswers++;
            }
        }

        int numberOfQuestions = attemptContent.size();

        return Mark.getMark(numberOfRightAnswers * 100 / numberOfQuestions);
    }

    private boolean isAnswerRight(Question question, Answer answer) {
        Multimap<Question, Answer> quizContent = dao.get().getContent();
        return quizContent.get(question)
                       .stream()
                       .filter(a -> a.getContent().equals(answer.getContent()))
                       .map(Answer::isCorrect)
                       .findFirst()
                       .orElse(false);
    }
}
