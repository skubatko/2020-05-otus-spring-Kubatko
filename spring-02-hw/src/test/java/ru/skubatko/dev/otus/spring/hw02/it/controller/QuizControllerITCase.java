package ru.skubatko.dev.otus.spring.hw02.it.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.skubatko.dev.otus.spring.hw02.it.TestConfig.OUTPUT;
import static ru.skubatko.dev.otus.spring.hw02.it.TestConfig.PARTICIPANT_NAME;

import ru.skubatko.dev.otus.spring.hw02.controller.QuizController;
import ru.skubatko.dev.otus.spring.hw02.it.IntegrationTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class QuizControllerITCase {

    @Autowired
    private QuizController controller;

    @Test
    public void getParticipantName() {
        String actual = controller.getParticipantName();

        assertThat(actual).isNotBlank().isEqualTo(PARTICIPANT_NAME);
        assertThat(OUTPUT.toString()).contains("Please enter your name:");
    }

    @Test
    public void makeQuizzed() {
        controller.makeQuizzed(PARTICIPANT_NAME);

        assertThat(OUTPUT.toString())
                .contains("Q:")
                .contains(PARTICIPANT_NAME)
                .contains("your result is");
    }
}
