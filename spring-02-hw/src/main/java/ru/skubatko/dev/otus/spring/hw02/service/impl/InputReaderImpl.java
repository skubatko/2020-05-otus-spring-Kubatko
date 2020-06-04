package ru.skubatko.dev.otus.spring.hw02.service.impl;

import ru.skubatko.dev.otus.spring.hw02.service.InputReader;

import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class InputReaderImpl implements InputReader {

    private static Scanner READER = new Scanner(System.in);

    @Override
    public String nextLine() {
        return READER.nextLine();
    }

    @Override
    public int nextInt() {
        return READER.nextInt();
    }
}
