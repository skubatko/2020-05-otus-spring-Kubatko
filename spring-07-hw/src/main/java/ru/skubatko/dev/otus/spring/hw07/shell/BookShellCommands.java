package ru.skubatko.dev.otus.spring.hw07.shell;

import ru.skubatko.dev.otus.spring.hw07.domain.Author;
import ru.skubatko.dev.otus.spring.hw07.domain.Book;
import ru.skubatko.dev.otus.spring.hw07.domain.Genre;
import ru.skubatko.dev.otus.spring.hw07.service.AuthorService;
import ru.skubatko.dev.otus.spring.hw07.service.BookService;
import ru.skubatko.dev.otus.spring.hw07.service.GenreService;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.Map;
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

        Author author = authorService.findById(book.getAuthorId());
        Genre genre = genreService.findById(book.getGenreId());

        return String.format("Book: %s \"%s\" by %s", genre.getName(), book.getName(), author.getName());
    }

    @ShellMethod(value = "Find all books in the library", key = {"fab", "findAllBooks"})
    @ShellMethodAvailability(value = "loggedIn")
    public String findAllBooks() {
        List<Book> books = bookService.findAll();
        Map<Long, Author> authors = authorService.findAll().stream().collect(Collectors.toMap(Author::getId, a -> a));
        Map<Long, Genre> genres = genreService.findAll().stream().collect(Collectors.toMap(Genre::getId, g -> g));

        return String.format("Available books: %n%s",
                books.stream()
                        .map(book -> String.join(
                                StringUtils.SPACE,
                                genres.get(book.getGenreId()).getName(),
                                "\"" + book.getName() + "\"",
                                "by",
                                authors.get(book.getAuthorId()).getName()
                        ))
                        .collect(Collectors.joining("\n")));
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

    @ShellMethod(value = "Delete book by id", key = {"db", "deleteBook"})
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
