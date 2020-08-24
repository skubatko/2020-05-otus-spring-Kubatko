package ru.skubatko.dev.otus.spring.hw22.service;

import ru.skubatko.dev.otus.spring.hw22.domain.Author;
import ru.skubatko.dev.otus.spring.hw22.domain.Book;
import ru.skubatko.dev.otus.spring.hw22.domain.Comment;
import ru.skubatko.dev.otus.spring.hw22.domain.Genre;
import ru.skubatko.dev.otus.spring.hw22.dto.BookDto;
import ru.skubatko.dev.otus.spring.hw22.exception.library.service.AuthorNotFoundLibraryServiceException;
import ru.skubatko.dev.otus.spring.hw22.exception.library.service.BookAlreadyExistsLibraryServiceException;
import ru.skubatko.dev.otus.spring.hw22.exception.library.service.BookNotFoundLibraryServiceException;
import ru.skubatko.dev.otus.spring.hw22.exception.library.service.CommentNotFoundLibraryServiceException;
import ru.skubatko.dev.otus.spring.hw22.exception.library.service.GenreNotFoundLibraryServiceException;
import ru.skubatko.dev.otus.spring.hw22.repository.AuthorRepository;
import ru.skubatko.dev.otus.spring.hw22.repository.BookRepository;
import ru.skubatko.dev.otus.spring.hw22.repository.CommentRepository;
import ru.skubatko.dev.otus.spring.hw22.repository.GenreRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public BookDto findBookByName(String bookName) {
        return BookDto.toDto(bookRepository.findByName(bookName));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAllBooks() {
        return bookRepository.findAll().stream().map(BookDto::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findBooksByAuthor(String authorName) {
        Author author = authorRepository.findByName(authorName);
        return bookRepository.findByAuthor(author).stream().map(BookDto::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findBooksByGenre(String genreName) {
        Genre genre = genreRepository.findByName(genreName);
        return bookRepository.findByGenre(genre).stream().map(BookDto::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addBook(BookDto book) {
        String bookName = book.getName();
        Book entity = bookRepository.findByName(bookName);
        if (entity != null) {
            throw new BookAlreadyExistsLibraryServiceException();
        }

        String authorName = book.getAuthor();
        Author author = authorRepository.findByName(authorName);
        if (author == null) {
            author = new Author(authorName);
            authorRepository.save(author);
        }

        String genreName = book.getGenre();
        Genre genre = genreRepository.findByName(genreName);
        if (genre == null) {
            genre = new Genre(genreName);
            genreRepository.save(genre);
        }

        entity = new Book(bookName, author, genre);
        bookRepository.save(entity);
    }

    @Override
    @Transactional
    public void addBookComment(String bookName, String commentContent) {
        Book book = getBookByName(bookName);

        Comment comment = new Comment(book, commentContent);
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void updateBook(String oldBookName, String newBookName) {
        Book book = getBookByName(oldBookName);

        book.setName(newBookName);
        bookRepository.save(book);
    }

    @Override
    @Transactional
    public void updateAuthor(String oldAuthorName, String newAuthorName) {
        Author author = authorRepository.findByName(oldAuthorName);
        if (author == null) {
            throw new AuthorNotFoundLibraryServiceException();
        }

        author.setName(newAuthorName);
        authorRepository.save(author);
    }

    @Override
    @Transactional
    public void updateGenre(String oldGenreName, String newGenreName) {
        Genre genre = genreRepository.findByName(oldGenreName);
        if (genre == null) {
            throw new GenreNotFoundLibraryServiceException();
        }

        genre.setName(newGenreName);
        genreRepository.save(genre);
    }

    @Override
    @Transactional
    public void updateBookComment(String bookName, String oldCommentContent, String newCommentContent) {
        Comment comment = getBookComment(bookName, oldCommentContent);

        comment.setContent(newCommentContent);
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteBook(String bookName) {
        Book book = getBookByName(bookName);

        bookRepository.delete(book);
    }

    @Override
    @Transactional
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
