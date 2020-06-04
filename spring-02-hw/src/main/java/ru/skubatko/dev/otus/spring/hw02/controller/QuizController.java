package ru.skubatko.dev.otus.spring.hw02.controller;

import java.io.InputStream;
import java.io.PrintStream;

public interface QuizController {

    void setIn(InputStream in);

    void setOut(PrintStream out);

    String getParticipantName();

    void makeQuizzed(String participantName);
}
