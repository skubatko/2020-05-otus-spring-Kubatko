package ru.skubatko.dev.otus.spring.hw05.domain;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.Data;

@Data
public class Quiz {
    private Multimap<Question, Answer> content = HashMultimap.create();
}
