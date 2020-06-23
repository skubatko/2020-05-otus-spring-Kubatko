package ru.skubatko.dev.otus.spring.hw05.aspect;

import ru.skubatko.dev.otus.spring.hw05.service.BenchmarkingService;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@RequiredArgsConstructor
public class BenchmarkingAspect {

    private final BenchmarkingService service;

    @Around("@annotation(ru.skubatko.dev.otus.spring.hw05.aspect.Benchmark)")
    public Object benchmarkAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch clock = new StopWatch(joinPoint.toString());
        try {
            clock.start(joinPoint.toShortString());
            return joinPoint.proceed();
        } finally {
            clock.stop();
            service.report(clock);
        }
    }
}
