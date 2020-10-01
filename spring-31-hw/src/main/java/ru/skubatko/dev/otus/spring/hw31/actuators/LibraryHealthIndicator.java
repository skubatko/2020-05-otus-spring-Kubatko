package ru.skubatko.dev.otus.spring.hw31.actuators;

import ru.skubatko.dev.otus.spring.hw31.service.LibraryService;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LibraryHealthIndicator implements HealthIndicator {

    private final LibraryService libraryService;

    private static final int LIBRARY_HEALTH_INDICATOR_MIN_VALUE = 5;

    @Override
    public Health health() {
        if (libraryService.countBooks() < LIBRARY_HEALTH_INDICATOR_MIN_VALUE) {
            return Health.down()
                           .status(Status.DOWN)
                           .withDetail("message", "Книжек малова-то осталось в библиотеке, нужно пополнить!")
                           .build();
        } else {
            return Health.up().withDetail("message", "Библиотека работает в штатном режиме!").build();
        }
    }
}
