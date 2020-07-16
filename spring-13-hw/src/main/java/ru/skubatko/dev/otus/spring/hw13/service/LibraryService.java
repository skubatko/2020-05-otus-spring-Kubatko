package ru.skubatko.dev.otus.spring.hw13.service;

import ru.skubatko.dev.otus.spring.hw13.domain.Book;
import ru.skubatko.dev.otus.spring.hw13.domain.Comment;
import ru.skubatko.dev.otus.spring.hw13.dto.BookDto;
import ru.skubatko.dev.otus.spring.hw13.exception.library.service.BookAlreadyExistsLibraryServiceException;
import ru.skubatko.dev.otus.spring.hw13.exception.library.service.BookNotFoundLibraryServiceException;
import ru.skubatko.dev.otus.spring.hw13.exception.library.service.CommentNotFoundLibraryServiceException;
import ru.skubatko.dev.otus.spring.hw13.repository.BookRepository;
import ru.skubatko.dev.otus.spring.hw13.repository.CommentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    public BookDto findBookByName(String bookName) {
        return BookDto.toDto(bookRepository.findByName(bookName));
    }

    public List<BookDto> findAllBooks() {
        return bookRepository.findAll().stream().map(BookDto::toDto).collect(Collectors.toList());
    }

    public List<BookDto> findBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author).stream().map(BookDto::toDto).collect(Collectors.toList());
    }

    public List<BookDto> findBooksByGenre(String genre) {
        return bookRepository.findByGenre(genre).stream().map(BookDto::toDto).collect(Collectors.toList());
    }

    public void addBook(String bookName, String author, String genre) {
        Book book = bookRepository.findByName(bookName);
        if (book != null) {
            throw new BookAlreadyExistsLibraryServiceException();
        }

        book = new Book(bookName, author, genre);
        bookRepository.save(book);
    }

    public void addBookComment(String bookName, String commentContent) {
        Book book = getBookByName(bookName);

        Comment comment = new Comment(commentContent, book);
        commentRepository.save(comment);

        book.getComments().add(comment);
        bookRepository.save(book);
    }

    public void updateBook(String oldBookName, String newBookName) {
        Book book = getBookByName(oldBookName);

        book.setName(newBookName);
        bookRepository.save(book);
    }

    public void updateAuthor(String oldAuthor, String newAuthor) {
        List<Book> books = bookRepository.findByAuthor(oldAuthor);

        books.forEach(book -> {
            book.setAuthor(newAuthor);
            bookRepository.save(book);
        });
    }

    public void updateGenre(String oldGenre, String newGenre) {
        List<Book> books = bookRepository.findByGenre(oldGenre);

        books.forEach(book -> {
            book.setGenre(newGenre);
            bookRepository.save(book);
        });
    }

    public void updateBookComment(String bookName, String oldCommentContent, String newCommentContent) {
        Comment comment = getBookComment(bookName, oldCommentContent);

        comment.setContent(newCommentContent);
        commentRepository.save(comment);
    }

    public void deleteBook(String bookName) {
        bookRepository.delete(getBookByName(bookName));
    }

    public void deleteBookComment(String bookName, String commentContent) {
        Comment comment = getBookComment(bookName, commentContent);
        Book book = comment.getBook();
        book.getComments().remove(comment);
        bookRepository.save(book);
    }

    private Comment getBookComment(String bookName, String commentContent) {
        Book book = getBookByName(bookName);
        Comment comment = commentRepository.findByBookAndContent(book, commentContent);
        if (comment == null) {
            throw new CommentNotFoundLibraryServiceException();
        }

        return comment;
    }

    private Book getBookByName(String bookName) {
        Book book = bookRepository.findByName(bookName);
        if (book == null) {
            throw new BookNotFoundLibraryServiceException();
        }

        return book;
    }
}
