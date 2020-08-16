package ru.skubatko.dev.otus.spring.hw20.config;

import com.github.cloudyrock.mongock.Mongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    private static final String CHANGELOGS_PACKAGE = "ru.skubatko.dev.otus.spring.hw20.changelogs";

    @Bean
    public Mongock mongock(MongoProps props, MongoClient mongoClient) {
        return new SpringMongockBuilder(mongoClient, props.getDatabase(), CHANGELOGS_PACKAGE).build();
    }
}
