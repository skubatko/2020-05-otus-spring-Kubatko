package ru.skubatko.dev.otus.spring.hw28.domain;

import ru.skubatko.dev.otus.spring.hw28.enums.BarberItemType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BarberItem {

    private BarberItemType content;
}
