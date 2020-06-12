package ru.skubatko.dev.otus.spring.hw04.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import ru.skubatko.dev.otus.spring.hw04.dao.QuizDao;
import ru.skubatko.dev.otus.spring.hw04.domain.Answer;
import ru.skubatko.dev.otus.spring.hw04.domain.Question;
import ru.skubatko.dev.otus.spring.hw04.domain.Quiz;
import ru.skubatko.dev.otus.spring.hw04.domain.QuizAttempt;
import ru.skubatko.dev.otus.spring.hw04.enums.Mark;
import ru.skubatko.dev.otus.spring.hw04.service.impl.QuizServiceImpl;

import com.google.common.collect.Multimap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTest {

    @Mock
    private QuizDao dao;

    @InjectMocks
    private QuizServiceImpl service;

    private Quiz quiz;
    private Question testQuestion1 = new Question("testQuestion1");
    private Question testQuestion2 = new Question("testQuestion2");
    private Question testQuestion3 = new Question("testQuestion3");
    private Question testQuestion4 = new Question("testQuestion4");
    private Question testQuestion5 = new Question("testQuestion5");
    private Answer wrongAnswer = new Answer("wrong", false);
    private Answer rightAnswer = new Answer("right", true);

    @BeforeEach
    public void setUp() {
        quiz = new Quiz();
        Multimap<Question, Answer> content = quiz.getContent();
        content.put(testQuestion1, wrongAnswer);
        content.put(testQuestion1, rightAnswer);
        content.put(testQuestion2, wrongAnswer);
        content.put(testQuestion2, rightAnswer);
        content.put(testQuestion3, wrongAnswer);
        content.put(testQuestion3, rightAnswer);
        content.put(testQuestion4, wrongAnswer);
        content.put(testQuestion4, rightAnswer);
        content.put(testQuestion5, wrongAnswer);
        content.put(testQuestion5, rightAnswer);
    }

    @Test
    public void getQuiz() {
        when(dao.get()).thenReturn(quiz);

        Quiz actual = service.getQuiz();

        assertThat(actual).isNotNull().isEqualTo(quiz);

        verify(dao).get();
        verifyNoMoreInteractions(dao);
    }

    @Test
    public void getQuizAttemptMarkWithAllRight() {
        when(dao.get()).thenReturn(quiz);
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setParticipantName("testName");
        Map<Question, Answer> content = quizAttempt.getContent();
        content.put(testQuestion1, rightAnswer);
        content.put(testQuestion2, rightAnswer);
        content.put(testQuestion3, rightAnswer);
        content.put(testQuestion4, rightAnswer);
        content.put(testQuestion5, rightAnswer);

        Mark actual = service.getQuizAttemptMark(quizAttempt);

        assertThat(actual).isEqualTo(Mark.F);

        verify(dao, times(5)).get();
        verifyNoMoreInteractions(dao);
    }

    @Test
    public void getQuizAttemptMarkWithFourRight() {
        when(dao.get()).thenReturn(quiz);
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setParticipantName("testName");
        Map<Question, Answer> content = quizAttempt.getContent();
        content.put(testQuestion1, rightAnswer);
        content.put(testQuestion2, rightAnswer);
        content.put(testQuestion3, rightAnswer);
        content.put(testQuestion4, rightAnswer);
        content.put(testQuestion5, wrongAnswer);

        Mark actual = service.getQuizAttemptMark(quizAttempt);

        assertThat(actual).isEqualTo(Mark.D);

        verify(dao, times(5)).get();
        verifyNoMoreInteractions(dao);
    }

    @Test
    public void getQuizAttemptMarkWithThreeRight() {
        when(dao.get()).thenReturn(quiz);
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setParticipantName("testName");
        Map<Question, Answer> content = quizAttempt.getContent();
        content.put(testQuestion1, rightAnswer);
        content.put(testQuestion2, rightAnswer);
        content.put(testQuestion3, rightAnswer);
        content.put(testQuestion4, wrongAnswer);
        content.put(testQuestion5, wrongAnswer);

        Mark actual = service.getQuizAttemptMark(quizAttempt);

        assertThat(actual).isEqualTo(Mark.C);

        verify(dao, times(5)).get();
        verifyNoMoreInteractions(dao);
    }

    @Test
    public void getQuizAttemptMarkWithTwoRight() {
        when(dao.get()).thenReturn(quiz);
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setParticipantName("testName");
        Map<Question, Answer> content = quizAttempt.getContent();
        content.put(testQuestion1, rightAnswer);
        content.put(testQuestion2, rightAnswer);
        content.put(testQuestion3, wrongAnswer);
        content.put(testQuestion4, wrongAnswer);
        content.put(testQuestion5, wrongAnswer);

        Mark actual = service.getQuizAttemptMark(quizAttempt);

        assertThat(actual).isEqualTo(Mark.B);

        verify(dao, times(5)).get();
        verifyNoMoreInteractions(dao);
    }

    @Test
    public void getQuizAttemptMarkWithOneRight() {
        when(dao.get()).thenReturn(quiz);
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setParticipantName("testName");
        Map<Question, Answer> content = quizAttempt.getContent();
        content.put(testQuestion1, rightAnswer);
        content.put(testQuestion2, wrongAnswer);
        content.put(testQuestion3, wrongAnswer);
        content.put(testQuestion4, wrongAnswer);
        content.put(testQuestion5, wrongAnswer);

        Mark actual = service.getQuizAttemptMark(quizAttempt);

        assertThat(actual).isEqualTo(Mark.A);

        verify(dao, times(5)).get();
        verifyNoMoreInteractions(dao);
    }

    @Test
    public void getQuizAttemptMarkWithAllWrong() {
        when(dao.get()).thenReturn(quiz);
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setParticipantName("testName");
        Map<Question, Answer> content = quizAttempt.getContent();
        content.put(testQuestion1, wrongAnswer);
        content.put(testQuestion2, wrongAnswer);
        content.put(testQuestion3, wrongAnswer);
        content.put(testQuestion4, wrongAnswer);
        content.put(testQuestion5, wrongAnswer);

        Mark actual = service.getQuizAttemptMark(quizAttempt);

        assertThat(actual).isEqualTo(Mark.A);

        verify(dao, times(5)).get();
        verifyNoMoreInteractions(dao);
    }
}
