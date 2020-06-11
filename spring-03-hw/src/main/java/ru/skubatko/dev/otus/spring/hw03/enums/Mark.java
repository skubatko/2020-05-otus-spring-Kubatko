package ru.skubatko.dev.otus.spring.hw03.enums;

public enum Mark {
    A(0, 29), B(30, 59), C(60, 74), D(75, 90), F(91, 100);

    private int fromInclusive;
    private int toInclusive;

    Mark(int fromInclusive, int toInclusive) {
        this.fromInclusive = fromInclusive;
        this.toInclusive = toInclusive;
    }

    public static Mark getMark(int result) {
        if (F.fromInclusive <= result && result <= F.toInclusive) {
            return F;
        }

        if (D.fromInclusive <= result && result <= D.toInclusive) {
            return D;
        }

        if (C.fromInclusive <= result && result <= C.toInclusive) {
            return C;
        }

        if (B.fromInclusive <= result && result <= B.toInclusive) {
            return B;
        }

        return A;
    }
}
