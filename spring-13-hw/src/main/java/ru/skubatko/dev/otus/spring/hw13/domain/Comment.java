package ru.skubatko.dev.otus.spring.hw13.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    private String id;
    private String content;
    private Book book;

    public Comment(String content, Book book) {
        this.content = content;
        this.book = book;
    }
}
