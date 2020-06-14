package ru.skubatko.dev.otus.spring.hw04.it.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import ru.skubatko.dev.otus.spring.hw04.App;
import ru.skubatko.dev.otus.spring.hw04.dao.QuizDao;
import ru.skubatko.dev.otus.spring.hw04.domain.Answer;
import ru.skubatko.dev.otus.spring.hw04.domain.Question;
import ru.skubatko.dev.otus.spring.hw04.domain.Quiz;
import ru.skubatko.dev.otus.spring.hw04.it.IntegrationTest;

import com.google.common.collect.Multimap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.Set;

@IntegrationTest
@SpringBootTest(classes = App.class)
public class QuizDaoITCase {

    @Autowired
    private QuizDao dao;

    @Test
    public void get() {
        Quiz actual = dao.get();
        assertThat(actual).isNotNull();

        Multimap<Question, Answer> content = actual.getContent();
        assertThat(content).isNotNull();

        Set<Question> questions = content.keySet();
        assertThat(questions).isNotNull().isNotEmpty().hasSize(4);

        Collection<Answer> test1 = content.get(new Question("test1"));
        Collection<Answer> test2 = content.get(new Question("test2"));
        Collection<Answer> test3 = content.get(new Question("test3"));
        Collection<Answer> test4 = content.get(new Question("test4"));

        assertAll(
                () -> assertThat(test1).isNotNull().isNotEmpty().hasSize(4).contains(new Answer("right11", true)),
                () -> assertThat(test2).isNotNull().isNotEmpty().hasSize(4).contains(new Answer("right22", true)),
                () -> assertThat(test3).isNotNull().isNotEmpty().hasSize(4).contains(new Answer("right33", true)),
                () -> assertThat(test4).isNotNull().isNotEmpty().hasSize(4).contains(new Answer("right44", true))
        );
    }
}
