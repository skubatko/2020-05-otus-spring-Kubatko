package ru.skubatko.dev.otus.spring.hw02.it;

import ru.skubatko.dev.otus.spring.hw02.App;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(
        basePackages = {"ru.skubatko.dev.otus.spring.hw02"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = App.class)}
)
@PropertySource("classpath:application-test.properties")
public class TestConfig {
}
