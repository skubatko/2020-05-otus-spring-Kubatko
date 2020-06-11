package ru.skubatko.dev.otus.spring.hw03.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(public * *(..)) && within(@ru.skubatko.dev.otus.spring.hw03.aspect.Logging *)")
    public Object logAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();

        log.debug("{}.{}() - start: args = {}", className, methodName, Arrays.toString(joinPoint.getArgs()));

        Object result = joinPoint.proceed();

        log.debug("{}.{}() - end: result = {}", className, methodName, result);

        return result;
    }
}
