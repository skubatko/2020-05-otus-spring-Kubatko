package ru.skubatko.dev.otus.spring.hw16.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app")
public class AppProps {

    private boolean h2ConsoleEnabled;
}
