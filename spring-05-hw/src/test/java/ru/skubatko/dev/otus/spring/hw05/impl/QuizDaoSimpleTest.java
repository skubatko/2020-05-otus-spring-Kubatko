package ru.skubatko.dev.otus.spring.hw05.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ru.skubatko.dev.otus.spring.hw05.config.AppProps;
import ru.skubatko.dev.otus.spring.hw05.dao.impl.QuizDaoSimple;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class QuizDaoSimpleTest {

    @Test
    public void initFailed() {
        assertThatThrownBy(() -> new QuizDaoSimple(new AppProps(), new ReloadableResourceBundleMessageSource()))
                .isInstanceOf(RuntimeException.class);
    }
}
