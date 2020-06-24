package ru.skubatko.dev.otus.spring.hw07.config;

import org.h2.tools.Console;
import org.springframework.context.annotation.Configuration;

@Configuration
public class H2ConsoleConfig {

    public H2ConsoleConfig(AppProps props) throws Exception {
        if (props.isH2ConsoleEnabled()) {
            Console.main();
        }
    }
}
