package ru.skubatko.dev.otus.spring.hw02.controller;

public interface QuizController {

    String getParticipantName();

    void makeQuizzed(String participantName);
}
