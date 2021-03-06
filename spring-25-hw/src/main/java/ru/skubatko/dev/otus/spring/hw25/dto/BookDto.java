package ru.skubatko.dev.otus.spring.hw25.dto;

import ru.skubatko.dev.otus.spring.hw25.model.sql.SqlBook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BookDto {

    private String name;
    private String author;
    private String genre;
    private String comments;

    public static BookDto toDto(SqlBook book) {
        if (book == null) {
            return null;
        }

        return new BookDto()
                       .setName(book.getName())
                       .setAuthor(book.getAuthor().getName())
                       .setGenre(book.getGenre().getName())
                       .setComments(book.getComments().stream()
                                            .map(CommentDto::toDto)
                                            .map(CommentDto::getContent)
                                            .collect(Collectors.joining(", ")));
    }
}
