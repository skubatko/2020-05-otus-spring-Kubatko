package ru.skubatko.dev.otus.spring.hw03.service.impl;

import ru.skubatko.dev.otus.spring.hw03.service.OutputPrinter;

import org.springframework.stereotype.Service;

import java.io.PrintStream;

@Service
public class OutputPrinterImpl implements OutputPrinter {

    private static PrintStream PRINTER = System.out;

    @Override
    public void println(String s) {
        PRINTER.println(s);
    }

    @Override
    public void printf(String s, Object... args) {
        PRINTER.printf(s, args);
    }
}
