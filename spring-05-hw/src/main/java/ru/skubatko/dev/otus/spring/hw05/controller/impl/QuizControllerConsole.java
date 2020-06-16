package ru.skubatko.dev.otus.spring.hw05.controller.impl;

import ru.skubatko.dev.otus.spring.hw05.aspect.Benchmark;
import ru.skubatko.dev.otus.spring.hw05.aspect.Logging;
import ru.skubatko.dev.otus.spring.hw05.controller.QuizController;
import ru.skubatko.dev.otus.spring.hw05.domain.Answer;
import ru.skubatko.dev.otus.spring.hw05.domain.Question;
import ru.skubatko.dev.otus.spring.hw05.domain.QuizAttempt;
import ru.skubatko.dev.otus.spring.hw05.enums.Mark;
import ru.skubatko.dev.otus.spring.hw05.service.InputReader;
import ru.skubatko.dev.otus.spring.hw05.service.OutputPrinter;
import ru.skubatko.dev.otus.spring.hw05.service.QuizService;

import com.google.common.collect.Multimap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Logging
@Controller
@RequiredArgsConstructor
public class QuizControllerConsole implements QuizController {

    private final QuizService service;
    private final InputReader reader;
    private final OutputPrinter printer;

    @Benchmark
    @Override
    public void makeQuizzed() {
        String participantName = getParticipantName();

        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setParticipantName(participantName);

        Multimap<Question, Answer> quizContent = service.getQuiz().getContent();
        Set<Question> questions = quizContent.keySet();
        for (Question question : questions) {
            printer.printf("Q: %s%n", question.getContent());

            List<Answer> answers = new ArrayList<>(quizContent.get(question));
            for (int i = 0; i < answers.size(); i++) {
                printer.printf("[%d] %s%n", i + 1, answers.get(i).getContent());
            }

            Answer answer = answers.get(getReply(answers));
            quizAttempt.getContent().put(question, answer);
        }

        Mark mark = service.getQuizAttemptMark(quizAttempt);
        printer.printf("%s, your result is: %s%n", participantName, mark);
    }

    private String getParticipantName() {
        printer.println("Please enter your name:");
        return reader.nextLine();
    }

    private int getReply(List<Answer> answers) {
        printer.println("Please enter answer number:");
        int reply = reader.nextInt();
        if (reply < 1) {
            reply = 1;
        }
        if (reply > answers.size()) {
            reply = answers.size();
        }

        return reply - 1;
    }
}
