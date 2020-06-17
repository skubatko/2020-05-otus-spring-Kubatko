package ru.skubatko.dev.otus.spring.hw05.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import ru.skubatko.dev.otus.spring.hw05.controller.impl.QuizControllerConsole;
import ru.skubatko.dev.otus.spring.hw05.domain.Answer;
import ru.skubatko.dev.otus.spring.hw05.domain.Question;
import ru.skubatko.dev.otus.spring.hw05.domain.Quiz;
import ru.skubatko.dev.otus.spring.hw05.domain.QuizAttempt;
import ru.skubatko.dev.otus.spring.hw05.enums.Mark;
import ru.skubatko.dev.otus.spring.hw05.service.InputReader;
import ru.skubatko.dev.otus.spring.hw05.service.OutputPrinter;
import ru.skubatko.dev.otus.spring.hw05.service.QuizService;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class QuizControllerTest {

    @Mock
    private QuizService service;
    @Mock
    private InputReader reader;
    @Mock
    private OutputPrinter printer;

    @InjectMocks
    private QuizControllerConsole controller;

    @Test
    public void makeQuizzed() {
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

        Mark mark = Mark.C;
        when(service.getQuizAttemptMark(any(QuizAttempt.class))).thenReturn(mark);

        when(reader.nextInt())
                .thenReturn(1)
                .thenReturn(2);

        controller.makeQuizzed("testUser");

        verify(service).getQuiz();
        verify(service).getQuizAttemptMark(any(QuizAttempt.class));

        verify(printer).printf("Q: %s%n", "q1");
        verify(printer).printf("Q: %s%n", "q2");
        verify(printer, times(2)).printf(eq("[%d] %s%n"), eq(1), anyString());
        verify(printer, times(2)).printf(eq("[%d] %s%n"), eq(2), anyString());
        verify(printer, times(2)).println("Please enter answer number:");

        verify(reader, times(2)).nextInt();

        verifyNoMoreInteractions(service, reader, printer);
    }
}
