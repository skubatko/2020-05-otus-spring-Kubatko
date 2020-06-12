package ru.skubatko.dev.otus.spring.hw04.service;

public interface OutputPrinter {

    void println(String s);

    void printf(String s, Object... args);
}
