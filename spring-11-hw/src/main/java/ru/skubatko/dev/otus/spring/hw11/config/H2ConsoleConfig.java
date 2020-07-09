package ru.skubatko.dev.otus.spring.hw11.config;

import org.h2.tools.Console;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class H2ConsoleConfig {

    public H2ConsoleConfig(AppProps props) throws SQLException {
        if (props.isH2ConsoleEnabled()) {
            Console.main();
        }
    }
}
