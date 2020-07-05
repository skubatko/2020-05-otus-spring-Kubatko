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
    public String findBookById(@ShellOption(defaultValue = "0") String stringId) {
        long id = Long.parseLong(stringId);

        Book book = bookService.findByIdWithComments(id);
        if (book == null) {
            return String.format("Book with id = %s not found", stringId);
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
        Book book = bookService.findByNameWithComments(name);
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
        List<Book> books = bookService.findAllWithComments();

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
    public String addBook(@ShellOption(defaultValue = "unnamed") String bookName,
                          @ShellOption(defaultValue = "people") String authorName,
                          @ShellOption(defaultValue = "general") String genreName) {
        Author author = new Author();
        author.setName(authorName);

        Genre genre = new Genre();
        genre.setName(genreName);

        Book book = new Book();
        book.setName(bookName);
        book.setAuthor(author);
        book.setGenre(genre);

        bookService.save(book);

        return String.format("Book %s added", bookName);
    }

    @ShellMethod(value = "Update book to the library", key = {"ub", "updateBook"})
    @ShellMethodAvailability(value = "loggedIn")
    public String updateBook(@ShellOption(defaultValue = "0") String idString,
                             @ShellOption(defaultValue = "unnamed") String name) {
        long id = Long.parseLong(idString);
        Book book = bookService.findById(id);
        if (book == null) {
            return String.format("Book with id = %s cannot be found", idString);
        }

        book.setName(name);
        bookService.update(book);
        return String.format("Book with id = %s updated", idString);
    }

    @ShellMethod(value = "Delete book by id", key = {"db", "deleteBook"})
    @ShellMethodAvailability(value = "loggedIn")
    public String deleteBookById(@ShellOption(defaultValue = "0") String idString) {
        long id = Long.parseLong(idString);
        if (bookService.findById(id) == null) {
            return String.format("Book with id = %s cannot be found", idString);
        }

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
