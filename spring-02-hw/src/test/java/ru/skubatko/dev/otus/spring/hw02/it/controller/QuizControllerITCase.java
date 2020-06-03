package ru.skubatko.dev.otus.spring.hw02.it.controller;

import static org.assertj.core.api.Assertions.assertThat;

import ru.skubatko.dev.otus.spring.hw02.controller.impl.QuizControllerImpl;
import ru.skubatko.dev.otus.spring.hw02.it.IntegrationTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class QuizControllerITCase {

    @Autowired
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

        String actual = controller.getParticipantName();

        assertThat(actual).isNotBlank().isEqualTo(participantName);
        assertThat(output.toString()).contains("Enter your name");
    }

    @Test
    public void makeQuizzed() {
        System.setIn(new ByteArrayInputStream("1\n1\n1\n1\n".getBytes()));

        controller.makeQuizzed(participantName);
        assertThat(output.toString()).contains("Q:");
    }
}
