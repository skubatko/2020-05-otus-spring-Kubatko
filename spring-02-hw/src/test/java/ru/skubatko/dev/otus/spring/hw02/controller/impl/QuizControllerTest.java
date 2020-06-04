package ru.skubatko.dev.otus.spring.hw02.controller.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import ru.skubatko.dev.otus.spring.hw02.domain.Answer;
import ru.skubatko.dev.otus.spring.hw02.domain.Question;
import ru.skubatko.dev.otus.spring.hw02.domain.Quiz;
import ru.skubatko.dev.otus.spring.hw02.domain.QuizAttempt;
import ru.skubatko.dev.otus.spring.hw02.enums.Mark;
import ru.skubatko.dev.otus.spring.hw02.service.QuizService;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@RunWith(MockitoJUnitRunner.class)
public class QuizControllerTest {

    @Mock
    private QuizService service;

    @InjectMocks
    private QuizControllerImpl controller;

    private String participantName = "testName";
    private ByteArrayOutputStream output;

    @Before
    public void setUp() {
        output = new ByteArrayOutputStream();
        controller.setOut(new PrintStream(output));
    }

    @Test
    public void getParticipantName() {
        controller.setIn(new ByteArrayInputStream(participantName.getBytes()));
        controller.getParticipantName();

        assertThat(output.toString())
                .isNotBlank()
                .contains("Enter your name");
    }

    @Test
    public void makeQuizzed() {
        controller.setIn(new ByteArrayInputStream("1 1 1 1 1".getBytes()));
        Multimap<Question, Answer> quizContent = HashMultimap.create();

        Question q1 = new Question("q1");
        quizContent.put(q1, new Answer("q1a1", true));
        quizContent.put(q1, new Answer("q1a2", false));

        Question q2 = new Question("q2");
        quizContent.put(q2, new Answer("q2a1", true));
        quizContent.put(q2, new Answer("q2a2", false));

        Quiz quiz = mock(Quiz.class);
        when(service.getQuiz()).thenReturn(quiz);
        when(quiz.getContent()).thenReturn(quizContent);

        when(service.getQuizAttemptMark(any(QuizAttempt.class))).thenReturn(Mark.C);

        controller.makeQuizzed(participantName);

        assertThat(output.toString())
                .isNotBlank()
                .contains("Q: q1")
                .contains("Q: q2")
                .contains("Your result is: C");

        verify(service).getQuiz();
        verify(service).getQuizAttemptMark(any(QuizAttempt.class));

        verifyNoMoreInteractions(service);
    }
}
