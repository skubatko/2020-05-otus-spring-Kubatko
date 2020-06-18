package ru.skubatko.dev.otus.spring.hw05.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import ru.skubatko.dev.otus.spring.hw05.dao.QuizDao;
import ru.skubatko.dev.otus.spring.hw05.domain.Answer;
import ru.skubatko.dev.otus.spring.hw05.domain.Question;
import ru.skubatko.dev.otus.spring.hw05.domain.Quiz;
import ru.skubatko.dev.otus.spring.hw05.domain.QuizAttempt;
import ru.skubatko.dev.otus.spring.hw05.enums.Mark;
import ru.skubatko.dev.otus.spring.hw05.service.impl.QuizServiceSimple;

import com.google.common.collect.Multimap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Map;

@DisplayName("Тест сервиса тестирования")
@SpringBootTest
public class QuizServiceTest {

    @MockBean
    private QuizDao dao;

    @Autowired
    private QuizServiceSimple service;

    private static final String PARTICIPANT_NAME = "testName";

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
        Multimap<Question, Answer> quizContent = quiz.getContent();
        quizContent.put(testQuestion1, wrongAnswer);
        quizContent.put(testQuestion1, rightAnswer);
        quizContent.put(testQuestion2, wrongAnswer);
        quizContent.put(testQuestion2, rightAnswer);
        quizContent.put(testQuestion3, wrongAnswer);
        quizContent.put(testQuestion3, rightAnswer);
        quizContent.put(testQuestion4, wrongAnswer);
        quizContent.put(testQuestion4, rightAnswer);
        quizContent.put(testQuestion5, wrongAnswer);
        quizContent.put(testQuestion5, rightAnswer);
    }

    @DisplayName("должен вернуть не пустой опросник и вызвать один раз DAO")
    @Test
    public void shouldReturnNotEmptyQuizWithDaoOneTimeFire() {
        given(dao.get()).willReturn(quiz);

        Quiz actual = service.getQuiz();

        assertThat(actual).isNotNull().isEqualTo(quiz);

        verify(dao, times(1)).get();
        verifyNoMoreInteractions(dao);
    }

    @DisplayName("при всех верных ответах должен вернуть оценку F")
    @Test
    public void whenAllAnswersRight_shouldReturnMarkF() {
        when(dao.get()).thenReturn(quiz);

        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setParticipantName(PARTICIPANT_NAME);
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

    @DisplayName("при одном неверном ответе из пяти должен вернуть оценку D")
    @Test
    public void whenOneOfFiveAnswerWrong_shouldReturnMarkD() {
        when(dao.get()).thenReturn(quiz);
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setParticipantName(PARTICIPANT_NAME);
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

    @DisplayName("при двух неверных ответах из пяти должен вернуть оценку C")
    @Test
    public void whenTwoOfFiveAnswersWrong_shouldReturnMarkC() {
        when(dao.get()).thenReturn(quiz);
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setParticipantName(PARTICIPANT_NAME);
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

    @DisplayName("при трех неверных ответах из пяти должен вернуть оценку B")
    @Test
    public void whenThreeOfFiveAnswersWrong_shouldReturnMarkB() {
        when(dao.get()).thenReturn(quiz);
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setParticipantName(PARTICIPANT_NAME);
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

    @DisplayName("при одном верном ответе из пяти должен вернуть оценку A")
    @Test
    public void whenOneOfFiveAnswerRight_shouldReturnMarkA() {
        when(dao.get()).thenReturn(quiz);
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setParticipantName(PARTICIPANT_NAME);
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

    @DisplayName("при всех неверных ответах должен вернуть оценку A")
    @Test
    public void whenAllAnswersWrong_shouldReturnMarkA() {
        when(dao.get()).thenReturn(quiz);
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setParticipantName(PARTICIPANT_NAME);
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
