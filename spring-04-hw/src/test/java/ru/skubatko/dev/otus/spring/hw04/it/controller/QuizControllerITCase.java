package ru.skubatko.dev.otus.spring.hw04.it.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.skubatko.dev.otus.spring.hw04.it.TestConfig.OUTPUT;
import static ru.skubatko.dev.otus.spring.hw04.it.TestConfig.PARTICIPANT_NAME;

import ru.skubatko.dev.otus.spring.hw04.App;
import ru.skubatko.dev.otus.spring.hw04.controller.QuizController;
import ru.skubatko.dev.otus.spring.hw04.it.IntegrationTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@IntegrationTest
@SpringBootTest(classes = App.class)
public class QuizControllerITCase {

    @Autowired
    private QuizController controller;

    @Test
    public void makeQuizzed() {
        controller.makeQuizzed();

        assertThat(OUTPUT.toString())
                .contains("Q:")
                .contains(PARTICIPANT_NAME)
                .contains("your result is");
    }
}
