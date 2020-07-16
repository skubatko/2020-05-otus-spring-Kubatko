package ru.skubatko.dev.otus.spring.hw13.dto;

import ru.skubatko.dev.otus.spring.hw13.domain.Book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
                       .setAuthor(book.getAuthor())
                       .setGenre(book.getGenre())
                       .setComments(book.getComments().stream()
                                            .map(CommentDto::toDto)
                                            .collect(Collectors.toList()));
    }
}
