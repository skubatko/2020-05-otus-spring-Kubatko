package ru.skubatko.dev.otus.spring.hw07.shell;

import ru.skubatko.dev.otus.spring.hw07.domain.Book;
import ru.skubatko.dev.otus.spring.hw07.service.BookService;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class BookShellCommands {

    private final BookService bookService;
    private final LoginShellCommands loginShellCommands;

    @ShellMethod(value = "Find book by id", key = {"fb", "findBookById"})
    @ShellMethodAvailability(value = "loggedIn")
    public String findBookById(@ShellOption(defaultValue = "0") String id) {
        Book book = bookService.findById(Long.parseLong(id));
        if (book == null) {
            return String.format("Book with id = %s not found", id);
        }
        return String.format("Book: %s", book.getName());
    }

    @ShellMethod(value = "Find all books in the library", key = {"fab", "findAllBooks"})
    @ShellMethodAvailability(value = "loggedIn")
    public String findAllBooks() {
        return String.format("Available books: %s",
                bookService.findAll().stream().map(Book::getName).collect(Collectors.joining(", ")));
    }

    @ShellMethod(value = "Add book to the library", key = {"ab", "addBook"})
    @ShellMethodAvailability(value = "loggedIn")
    public String addBook(@ShellOption(defaultValue = "0") String id,
                          @ShellOption(defaultValue = "unnamed") String name,
                          @ShellOption(defaultValue = "0") String authorId,
                          @ShellOption(defaultValue = "0") String genreId) {
        int response = bookService.insert(
                new Book(Long.parseLong(id), name, Long.parseLong(authorId), Long.parseLong(genreId)));
        if (response != 1) {
            return "Book cannot be added";
        }

        return String.format("Book %s added successfully", name);
    }

    @ShellMethod(value = "Update book to the library", key = {"ub", "updateBook"})
    @ShellMethodAvailability(value = "loggedIn")
    public String updateBook(@ShellOption(defaultValue = "0") String idString,
                             @ShellOption(defaultValue = "unnamed") String name) {
        long id = Long.parseLong(idString);
        Book book = bookService.findById(id);
        book.setName(name);
        int response = bookService.update(book);
        if (response != 1) {
            return "Book cannot be updated";
        }

        return String.format("Book %s updated successfully", name);
    }

    @ShellMethod(value = "Delete book by id", key = {"db", "deleteBookById"})
    @ShellMethodAvailability(value = "loggedIn")
    public String deleteBookById(@ShellOption(defaultValue = "0") String idString) {
        long id = Long.parseLong(idString);
        String name = bookService.findById(id).getName();
        int response = bookService.deleteById(id);
        if (response != 1) {
            return "Book cannot be deleted";
        }

        return String.format("Book %s deleted successfully", name);
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
