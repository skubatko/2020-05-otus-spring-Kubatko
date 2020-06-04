package ru.skubatko.dev.otus.spring.hw02.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import ru.skubatko.dev.otus.spring.hw02.domain.Answer;
import ru.skubatko.dev.otus.spring.hw02.domain.Question;
import ru.skubatko.dev.otus.spring.hw02.domain.Quiz;
import ru.skubatko.dev.otus.spring.hw02.domain.QuizAttempt;
import ru.skubatko.dev.otus.spring.hw02.enums.Mark;
import ru.skubatko.dev.otus.spring.hw02.service.InputReader;
import ru.skubatko.dev.otus.spring.hw02.service.OutputPrinter;
import ru.skubatko.dev.otus.spring.hw02.service.QuizService;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QuizControllerTest {

    @Mock
    private QuizService service;
    @Mock
    private InputReader reader;
    @Mock
    private OutputPrinter printer;

    @InjectMocks
    private QuizControllerImpl controller;

    @Captor
    ArgumentCaptor<String> printerArgCaptor;

    private String participantName = "testName";

    @Test
    public void getParticipantName() {
        when(reader.nextLine()).thenReturn(participantName);

        String actual = controller.getParticipantName();

        assertThat(actual).isNotBlank().isEqualTo(participantName);

        verify(printer).println(printerArgCaptor.capture());
        assertThat(printerArgCaptor.getValue()).isEqualTo("Please enter your name:");

        verify(reader).nextLine();

        verifyNoMoreInteractions(service, reader, printer);
    }

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

        controller.makeQuizzed(participantName);

        verify(service).getQuiz();
        verify(service).getQuizAttemptMark(any(QuizAttempt.class));

        verify(printer).printf("Q: %s%n", "q1");
        verify(printer).printf("Q: %s%n", "q2");
        verify(printer, times(2)).printf(eq("[%d] %s%n"), eq(1), anyString());
        verify(printer, times(2)).printf(eq("[%d] %s%n"), eq(2), anyString());
        verify(printer, times(2)).println("Please enter answer number:");
        verify(printer).printf("%s, your result is: %s%n", participantName, mark);

        verify(reader, times(2)).nextInt();

        verifyNoMoreInteractions(service, reader, printer);
    }
}
