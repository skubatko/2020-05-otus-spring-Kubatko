package ru.skubatko.dev.otus.spring.hw03.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ru.skubatko.dev.otus.spring.hw03.config.AppProps;
import ru.skubatko.dev.otus.spring.hw03.dao.impl.QuizDaoFile;
import ru.skubatko.dev.otus.spring.hw03.domain.Answer;
import ru.skubatko.dev.otus.spring.hw03.domain.Question;
import ru.skubatko.dev.otus.spring.hw03.domain.Quiz;

import com.google.common.collect.Multimap;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Set;

public class QuizDaoTest {

    @Test
    public void get() {
        AppProps props = new AppProps();
        props.setQuiz("quiz-test.csv");

        Quiz actual = new QuizDaoFile(props).get();

        assertThat(actual).isNotNull();

        Multimap<Question, Answer> content = actual.getContent();
        assertThat(content).isNotNull();

        Set<Question> questions = content.keySet();
        assertThat(questions)
                .isNotNull()
                .isNotEmpty()
                .hasSize(4);

        Answer right = new Answer("right", true);

        Collection<Answer> test1 = content.get(new Question("test1"));
        assertThat(test1)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .containsOnly(right, new Answer("wrong", false));

        Collection<Answer> test2 = content.get(new Question("test2"));
        assertThat(test2)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .containsOnly(right, new Answer("wrong1", false), new Answer("wrong2", false));

        Collection<Answer> test3 = content.get(new Question("test3"));
        assertThat(test3)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5)
                .containsOnly(right,
                        new Answer("wrong1", false),
                        new Answer("wrong2", false),
                        new Answer("wrong3", false),
                        new Answer("wrong4", false));

        Collection<Answer> test4 = content.get(new Question("test4"));
        assertThat(test4)
                .isNotNull()
                .isNotEmpty()
                .hasSize(6)
                .containsOnly(right,
                        new Answer("wrong1", false),
                        new Answer("wrong2", false),
                        new Answer("wrong3", false),
                        new Answer("wrong4", false),
                        new Answer("wrong5", false));
    }

    @Test
    public void initFailed() {
        assertThatThrownBy(() -> new QuizDaoFile(new AppProps())).isInstanceOf(RuntimeException.class);
    }
}
