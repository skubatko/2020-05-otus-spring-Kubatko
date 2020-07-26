package ru.skubatko.dev.otus.spring.hw16.dto;

import ru.skubatko.dev.otus.spring.hw16.domain.Book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BookDto {

    @NotBlank(message = "Book's name is mandatory")
    private String name;

    @NotBlank(message = "Author is mandatory")
    private String author;

    @NotBlank(message = "Genre is mandatory")
    private String genre;

    private String comments;

    public BookDto(String name, String author, String genre) {
        this.name = name;
        this.author = author;
        this.genre = genre;
    }

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
                                            .map(CommentDto::getContent)
                                            .collect(Collectors.joining(", ")));
    }
}
