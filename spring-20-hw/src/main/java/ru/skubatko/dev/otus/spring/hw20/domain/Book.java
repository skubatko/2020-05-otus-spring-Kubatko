package ru.skubatko.dev.otus.spring.hw20.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    private String id;
    private String name;
    private String author;
    private String genre;

    @DBRef
    private List<Comment> comments = new ArrayList<>();

    public Book(String name, String author, String genre, Comment... comments) {
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.comments = Arrays.asList(comments);
    }
}
