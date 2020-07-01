package ru.skubatko.dev.otus.spring.hw09.shell;

import ru.skubatko.dev.otus.spring.hw09.domain.Author;
import ru.skubatko.dev.otus.spring.hw09.domain.Book;
import ru.skubatko.dev.otus.spring.hw09.domain.BookComment;
import ru.skubatko.dev.otus.spring.hw09.domain.Genre;
import ru.skubatko.dev.otus.spring.hw09.service.AuthorService;
import ru.skubatko.dev.otus.spring.hw09.service.BookService;
import ru.skubatko.dev.otus.spring.hw09.service.GenreService;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class BookShellCommands {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final LoginShellCommands loginShellCommands;

    @ShellMethod(value = "Find book by id", key = {"fb", "findBook"})
    @ShellMethodAvailability(value = "loggedIn")
    public String findBookById(@ShellOption(defaultValue = "0") String id) {
        Book book = bookService.findById(Long.parseLong(id));
        if (book == null) {
            return String.format("Book with id = %s not found", id);
        }

        String comments = book.getBookComments().stream()
                                  .map(BookComment::getContent)
                                  .collect(Collectors.joining(", "));
        return String.format("Book: %s \"%s\" by %s has comment(s): %s",
                book.getGenre().getName(), book.getName(), book.getAuthor().getName(), comments);
    }

    @ShellMethod(value = "Find book by name", key = {"fbn", "findBookByName"})
    @ShellMethodAvailability(value = "loggedIn")
    public String findBookByName(@ShellOption(defaultValue = "unnamed") String name) {
        Book book = bookService.findByName(name);
        if (book == null) {
            return String.format("Book with name = %s not found", name);
        }

        String comments = book.getBookComments().stream()
                                  .map(BookComment::getContent)
                                  .collect(Collectors.joining(", "));
        return String.format("Book: %s \"%s\" by %s has comment(s): %s",
                book.getGenre().getName(), book.getName(), book.getAuthor().getName(), comments);
    }

    @ShellMethod(value = "Find all books in the library", key = {"fab", "findAllBooks"})
    @ShellMethodAvailability(value = "loggedIn")
    public String findAllBooks() {
        List<Book> books = bookService.findAll();

        return String.format("Available books: %n%s",
                books.stream()
                        .map(book -> String.join(
                                StringUtils.SPACE,
                                book.getGenre().getName(),
                                "\"" + book.getName() + "\"",
                                "by",
                                book.getAuthor().getName(),
                                "has comment(s):",
                                book.getBookComments().stream()
                                        .map(BookComment::getContent)
                                        .collect(Collectors.joining(", "))
                        ))
                        .collect(Collectors.joining("\n")));
    }

    @ShellMethod(value = "Add book to the library", key = {"ab", "addBook"})
    @ShellMethodAvailability(value = "loggedIn")
    public String addBook(@ShellOption(defaultValue = "0") String id,
                          @ShellOption(defaultValue = "unnamed") String name,
                          @ShellOption(defaultValue = "0") String authorId,
                          @ShellOption(defaultValue = "0") String genreId) {

        Author author = authorService.findById(Long.parseLong(authorId));
        Genre genre = genreService.findById(Long.parseLong(genreId));

        bookService.save(new Book(
                Long.parseLong(id),
                name,
                author,
                genre,
                Collections.emptyList()));

        return String.format("Book %s added successfully", name);
    }

    @ShellMethod(value = "Update book to the library", key = {"ub", "updateBook"})
    @ShellMethodAvailability(value = "loggedIn")
    public String updateBook(@ShellOption(defaultValue = "0") String idString,
                             @ShellOption(defaultValue = "unnamed") String name) {
        long id = Long.parseLong(idString);
        Book book = bookService.findById(id);
        book.setName(name);
        bookService.update(book);
        return String.format("Book %s updated successfully", name);
    }

    @ShellMethod(value = "Delete book by id", key = {"db", "deleteBook"})
    @ShellMethodAvailability(value = "loggedIn")
    public String deleteBookById(@ShellOption(defaultValue = "0") String idString) {
        long id = Long.parseLong(idString);
        bookService.deleteById(id);
        return String.format("Book with id = %s deleted successfully", idString);
    }

    @ShellMethod(value = "Get number of books in the library", key = {"cb", "countBooks"})
    @ShellMethodAvailability(value = "loggedIn")
    public String countBooks() {
        return String.format("In the library now %d book(s)", bookService.count());
    }

    private Availability loggedIn() {
        return loginShellCommands.loggedIn();
    }
}
