package ru.skubatko.dev.otus.spring.hw25.model.nosql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class NoSqlComment {

    @Id
    private String id;
    private String content;
    private String bookName;

    public NoSqlComment(String content, String bookName) {
        this.content = content;
        this.bookName = bookName;
    }
}
