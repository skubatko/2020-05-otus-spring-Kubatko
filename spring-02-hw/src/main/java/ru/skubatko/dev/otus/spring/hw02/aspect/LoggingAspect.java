package ru.skubatko.dev.otus.spring.hw02.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    @Around("@target(ru.skubatko.dev.otus.spring.hw02.aspect.Logger)")
    public Object logAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();

        System.out.printf("%s.%s() - start: args = %s%n", className, methodName, Arrays.toString(joinPoint.getArgs()));

        Object result = joinPoint.proceed();

        System.out.printf("%s.%s() - end: result = %s%n", className, methodName, result);

        return result;
    }
}
