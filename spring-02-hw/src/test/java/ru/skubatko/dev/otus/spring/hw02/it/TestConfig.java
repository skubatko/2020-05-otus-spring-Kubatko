package ru.skubatko.dev.otus.spring.hw02.it;

import ru.skubatko.dev.otus.spring.hw02.App;
import ru.skubatko.dev.otus.spring.hw02.service.InputReader;
import ru.skubatko.dev.otus.spring.hw02.service.OutputPrinter;
import ru.skubatko.dev.otus.spring.hw02.service.impl.InputReaderImpl;
import ru.skubatko.dev.otus.spring.hw02.service.impl.OutputPrinterImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@Configuration
@ComponentScan(
        basePackages = {"ru.skubatko.dev.otus.spring.hw02"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = App.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = InputReaderImpl.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = OutputPrinterImpl.class)
        }
)
@PropertySource("classpath:application-test.properties")
public class TestConfig {

    public static final String PARTICIPANT_NAME = "testName";
    public static final ByteArrayOutputStream OUTPUT = new ByteArrayOutputStream();

    @Bean
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
