package ru.skubatko.dev.otus.spring.hw28.service;

import ru.skubatko.dev.otus.spring.hw28.domain.BarberItem;
import ru.skubatko.dev.otus.spring.hw28.domain.Beauty;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@SuppressWarnings("unused")
public class BarberService {

    @SneakyThrows
    public Beauty beautify(BarberItem item) {
        log.info("beautify() - start: {}", item.getContent());
        Thread.sleep(1000);
        log.info("beautify() - end: {}", item.getContent());
        return new Beauty(item.getContent().name());
    }
}
