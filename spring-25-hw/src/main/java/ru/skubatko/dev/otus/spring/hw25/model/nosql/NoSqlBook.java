package ru.skubatko.dev.otus.spring.hw25.model.nosql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "books")
@NoArgsConstructor
@AllArgsConstructor
public class NoSqlBook {

    @Id
    private String id;
    private String name;
    private String author;
    private String genre;
    private List<NoSqlComment> comments = new ArrayList<>();

    public NoSqlBook(String name, String author, String genre, List<NoSqlComment> comments) {
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.comments = comments;
    }
}
