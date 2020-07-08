package ru.skubatko.dev.otus.spring.hw11.dto;

import ru.skubatko.dev.otus.spring.hw11.domain.Book;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class BookDto {
    private String name;
    private String author;
    private String genre;
    private List<CommentDto> comments;

    public static BookDto toDto(Book book) {
        if (book == null) {
            return null;
        }

        return new BookDto()
                       .setName(book.getName())
                       .setAuthor(book.getAuthor().getName())
                       .setGenre(book.getGenre().getName())
                       .setComments(book.getComments().stream()
                                            .map(CommentDto::toDto)
                                            .collect(Collectors.toList()));
    }
}
