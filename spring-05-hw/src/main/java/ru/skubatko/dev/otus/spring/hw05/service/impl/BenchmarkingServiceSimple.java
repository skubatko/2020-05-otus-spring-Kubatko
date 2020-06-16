package ru.skubatko.dev.otus.spring.hw05.service.impl;

import ru.skubatko.dev.otus.spring.hw05.service.BenchmarkingService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

@Slf4j
@Service
public class BenchmarkingServiceSimple implements BenchmarkingService {

    @Override
    @Async
    public void report(StopWatch clock) {
        log.debug(clock.prettyPrint());
    }
}
