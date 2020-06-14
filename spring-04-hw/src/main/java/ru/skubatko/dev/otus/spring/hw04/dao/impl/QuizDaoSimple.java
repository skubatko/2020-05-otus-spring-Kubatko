package ru.skubatko.dev.otus.spring.hw04.dao.impl;

import ru.skubatko.dev.otus.spring.hw04.config.AppProps;
import ru.skubatko.dev.otus.spring.hw04.dao.QuizDao;
import ru.skubatko.dev.otus.spring.hw04.domain.Answer;
import ru.skubatko.dev.otus.spring.hw04.domain.Question;
import ru.skubatko.dev.otus.spring.hw04.domain.Quiz;

import com.google.common.collect.Multimap;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import java.util.Locale;

@Repository
public class QuizDaoSimple implements QuizDao {
    private static final String QUESTION_TAG = "Q";
    private static final String ANSWER_TAG = "A";
    private static final String DOT = ".";
    public static final String[] ARGS = new String[0];

    private Quiz quiz;

    public QuizDaoSimple(AppProps props, MessageSource messageSource) {
        Locale locale = props.getLocale();

        int numberOfAnswersPerQuestion = props.getQuiz().getNumberOfAnswersPerQuestion();
        int numberOfQuestions = props.getQuiz().getNumberOfQuestions();

        quiz = new Quiz();
        Multimap<Question, Answer> quizContent = quiz.getContent();

        for (int q = 1; q <= numberOfQuestions; q++) {
            Question question = new Question(messageSource.getMessage(QUESTION_TAG + q, ARGS, locale));
            for (int a = 1; a <= numberOfAnswersPerQuestion; a++) {
                String codePrefix = QUESTION_TAG + q + ANSWER_TAG + a + DOT;
                String answerContent = messageSource.getMessage(codePrefix + "content", ARGS, locale);
                boolean correctness = Boolean.parseBoolean(messageSource.getMessage(codePrefix + "correctness", ARGS, locale));
                Answer answer = new Answer(answerContent, correctness);
                quizContent.put(question, answer);
            }
        }
    }

    @Override
    public Quiz get() {
        return quiz;
    }
}
