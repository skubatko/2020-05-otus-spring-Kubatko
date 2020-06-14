package ru.skubatko.dev.otus.spring.hw04.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

@Slf4j
@Service
public class BenchmarkingService {

    @Async
    public void report(StopWatch clock) {
        log.debug(clock.prettyPrint());
    }
}
