package ru.skubatko.dev.otus.spring.hw02.controller.impl;

import ru.skubatko.dev.otus.spring.hw02.controller.QuizController;
import ru.skubatko.dev.otus.spring.hw02.domain.Answer;
import ru.skubatko.dev.otus.spring.hw02.domain.Question;
import ru.skubatko.dev.otus.spring.hw02.domain.QuizAttempt;
import ru.skubatko.dev.otus.spring.hw02.enums.Mark;
import ru.skubatko.dev.otus.spring.hw02.service.QuizService;

import com.google.common.collect.Multimap;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

@Controller
@Setter
@RequiredArgsConstructor
public class QuizControllerImpl implements QuizController {
    private final QuizService service;

    private InputStream in = System.in;
    private PrintStream out = System.out;

    @Override
    public String getParticipantName() {
        Scanner sc = new Scanner(in);
        out.println("Enter your name (q - for exit):");
        return sc.nextLine();
    }

    @Override
    public void makeQuizzed(String participantName) {
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setParticipantName(participantName);

        Multimap<Question, Answer> quizContent = service.getQuiz().getContent();
        Set<Question> questions = quizContent.keySet();
        for (Question question : questions) {
            out.printf("Q: %s%n", question.getContent());

            List<Answer> answers = new ArrayList<>(quizContent.get(question));
            for (int i = 0; i < answers.size(); i++) {
                out.printf("[%d] %s%n", i + 1, answers.get(i).getContent());
            }

            Answer answer = answers.get(getReply(answers));
            quizAttempt.getContent().put(question, answer);
        }

        Mark mark = service.getQuizAttemptMark(quizAttempt);
        out.printf("Your result is: %s%n", mark);
    }

    private int getReply(List<Answer> answers) {
        Scanner sc = new Scanner(in);

        out.println("please enter answer number:");
        int reply = sc.nextInt();
        if (reply < 1) {
            reply = 1;
        }
        if (reply > answers.size()) {
            reply = answers.size();
        }

        return reply - 1;
    }
}
