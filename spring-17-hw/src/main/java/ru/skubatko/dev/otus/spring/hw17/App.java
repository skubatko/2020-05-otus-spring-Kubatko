package ru.skubatko.dev.otus.spring.hw17;

import ru.skubatko.dev.otus.spring.hw17.config.AppProps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProps.class)
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
