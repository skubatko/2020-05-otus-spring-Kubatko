package ru.skubatko.dev.otus.spring.hw20.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("spring.data.mongodb")
public class MongoProps {
    private String host;
    private int port;
    private String database;
}
