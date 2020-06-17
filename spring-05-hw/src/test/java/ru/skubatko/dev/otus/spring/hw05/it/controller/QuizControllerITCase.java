package ru.skubatko.dev.otus.spring.hw05.it.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.skubatko.dev.otus.spring.hw05.it.TestConfig.OUTPUT;
import static ru.skubatko.dev.otus.spring.hw05.it.TestConfig.PARTICIPANT_NAME;

import ru.skubatko.dev.otus.spring.hw05.App;
import ru.skubatko.dev.otus.spring.hw05.controller.QuizController;
import ru.skubatko.dev.otus.spring.hw05.it.IntegrationTest;

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
        controller.makeQuizzed(PARTICIPANT_NAME);

        assertThat(OUTPUT.toString())
                .contains("Q: test1")
                .contains("Q: test2")
                .contains("Q: test3")
                .contains("Q: test4");
    }
}
