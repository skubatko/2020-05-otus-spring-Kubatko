package ru.skubatko.dev.otus.spring.hw05.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;

@Data
@ConfigurationProperties(prefix = "app")
public class AppProps {

    private Quiz quiz;
    private Locale locale;

    @Data
    public static class Quiz {
        private int numberOfQuestions;
        private int numberOfAnswersPerQuestion;
    }
}
