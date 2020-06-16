package ru.skubatko.dev.otus.spring.hw05.enums;

import java.util.Arrays;

public enum Mark {
    A(0, 29), B(30, 59), C(60, 74), D(75, 90), F(91, 100);

    private int fromInclusive;
    private int toInclusive;

    Mark(int fromInclusive, int toInclusive) {
        this.fromInclusive = fromInclusive;
        this.toInclusive = toInclusive;
    }

    public static Mark getMark(int result) {
        return Arrays.stream(Mark.values())
                .filter(mark -> mark.fromInclusive <= result && result <= mark.toInclusive)
                .findFirst()
                .orElse(Mark.A);
    }
}
