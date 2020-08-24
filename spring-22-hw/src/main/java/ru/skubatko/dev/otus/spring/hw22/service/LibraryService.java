package ru.skubatko.dev.otus.spring.hw22.service;

import ru.skubatko.dev.otus.spring.hw22.dto.BookDto;

import java.util.List;

public interface LibraryService {

    BookDto findBookByName(String bookName);

    List<BookDto> findAllBooks();

    List<BookDto> findBooksByAuthor(String authorName);

    List<BookDto> findBooksByGenre(String genreName);

    void addBook(BookDto book);

    void addBookComment(String bookName, String commentContent);

    void updateBook(String oldBookName, String newBookName);

    void updateAuthor(String oldAuthorName, String newAuthorName);

    void updateGenre(String oldGenreName, String newGenreName);

    void updateBookComment(String bookName, String oldCommentContent, String newCommentContent);

    void deleteBook(String bookName);

    void deleteBookComment(String bookName, String commentContent);
}
