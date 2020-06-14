package ru.skubatko.dev.otus.spring.hw04.service;

import org.springframework.util.StopWatch;

public interface BenchmarkingService {

    void report(StopWatch clock);
}
