package ru.skubatko.dev.otus.spring.hw04.it;


import ru.skubatko.dev.otus.spring.hw04.service.InputReader;
import ru.skubatko.dev.otus.spring.hw04.service.OutputPrinter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@Configuration
public class TestConfig {

    public static final String PARTICIPANT_NAME = "testName";
    public static final ByteArrayOutputStream OUTPUT = new ByteArrayOutputStream();

    @Bean
    @Primary
    public InputReader reader() {
        return new InputReader() {
            @Override
            public String nextLine() {
                return PARTICIPANT_NAME;
            }

            @Override
            public int nextInt() {
                return 1;
            }
        };
    }

    @Bean
    @Primary
    public OutputPrinter printer() {
        return new OutputPrinter() {

            private PrintStream printStream = new PrintStream(OUTPUT);

            @Override
            public void println(String s) {
                printStream.println(s);
            }

            @Override
            public void printf(String s, Object... args) {
                printStream.printf(s, args);
            }
        };
    }
}
