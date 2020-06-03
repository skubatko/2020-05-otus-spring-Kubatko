package ru.skubatko.dev.otus.spring.hw02.it.controller;

import static org.assertj.core.api.Assertions.assertThat;

import ru.skubatko.dev.otus.spring.hw02.controller.impl.QuizControllerImpl;
import ru.skubatko.dev.otus.spring.hw02.it.IntegrationTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayInputStream;

@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class QuizControllerITCase {

    @Autowired
    private QuizControllerImpl controller;

    @Test
    public void getParticipantName() {
        String participantName = "testName";

        controller.setIn(new ByteArrayInputStream(participantName.getBytes()));

        String actual = controller.getParticipantName();

        assertThat(actual).isNotBlank().isEqualTo(participantName);
    }

    @Test
    public void makeQuizzed() {
        String participantName = "testName";
        System.setIn(new ByteArrayInputStream("1\n1\n1\n1\n".getBytes()));

        controller.makeQuizzed(participantName);
    }
}
