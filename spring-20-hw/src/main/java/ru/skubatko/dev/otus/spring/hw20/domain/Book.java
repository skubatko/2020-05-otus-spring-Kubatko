package ru.skubatko.dev.otus.spring.hw20.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    private String id;
    private String name;
    private String author;
    private String genre;
    private List<Comment> comments = new ArrayList<>();

    public Book(String name, String author, String genre, List<Comment> comments) {
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.comments = comments;
    }
}
