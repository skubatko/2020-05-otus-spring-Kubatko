package ru.skubatko.dev.otus.spring.hw11.shell;

import ru.skubatko.dev.otus.spring.hw11.dto.BookDto;
import ru.skubatko.dev.otus.spring.hw11.dto.CommentDto;
import ru.skubatko.dev.otus.spring.hw11.service.LibraryService;

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
public class LibraryShellCommands {

    private final LibraryService libraryService;
    private final LoginShellCommands loginShellCommands;

    @ShellMethod(value = "Find book by name", key = {"fbn", "findBookByName"})
    @ShellMethodAvailability(value = "loggedIn")
    public String findBookByName(@ShellOption(defaultValue = "unnamed") String bookName) {
        BookDto book = libraryService.findBookByName(bookName);
        if (book == null) {
            return String.format("Book with name = %s not found", bookName);
        }

        String comments = book.getComments().stream()
                                  .map(CommentDto::getContent)
                                  .collect(Collectors.joining(", "));

        return String.format("Book: %s \"%s\" by %s has comment(s): %s",
                book.getGenre(), book.getName(), book.getAuthor(), comments);
    }

    @ShellMethod(value = "Find all books available in the library", key = {"fab", "findAllBooks"})
    @ShellMethodAvailability(value = "loggedIn")
    public String findAllBooks() {
        List<BookDto> books = libraryService.findAllBooks();

        return String.format("Available books: %n%s",
                books.stream()
                        .map(book -> String.join(
                                StringUtils.SPACE,
                                book.getGenre(),
                                "\"" + book.getName() + "\"",
                                "by",
                                book.getAuthor(),
                                "has comment(s):",
                                book.getComments().stream()
                                        .map(CommentDto::getContent)
                                        .collect(Collectors.joining(", "))
                        ))
                        .collect(Collectors.joining("\n")));
    }

    @ShellMethod(value = "Find all books of the given author", key = {"fba", "findBooksByAuthor"})
    @ShellMethodAvailability(value = "loggedIn")
    public String findBooksByAuthor(@ShellOption(defaultValue = "undefined") String authorName) {
        List<BookDto> books = libraryService.findBooksByAuthor(authorName);

        return String.format("Available books: %n%s",
                books.stream()
                        .map(book -> String.join(
                                StringUtils.SPACE,
                                book.getGenre(),
                                "\"" + book.getName() + "\"",
                                "by",
                                book.getAuthor(),
                                "has comment(s):",
                                book.getComments().stream()
                                        .map(CommentDto::getContent)
                                        .collect(Collectors.joining(", "))
                        ))
                        .collect(Collectors.joining("\n")));
    }

    @ShellMethod(value = "Find all books of the given genre", key = {"fbg", "findBooksByGenre"})
    @ShellMethodAvailability(value = "loggedIn")
    public String findBooksByGenre(@ShellOption(defaultValue = "undefined") String genreName) {
        List<BookDto> books = libraryService.findBooksByGenre(genreName);

        return String.format("Available books: %n%s",
                books.stream()
                        .map(book -> String.join(
                                StringUtils.SPACE,
                                book.getGenre(),
                                "\"" + book.getName() + "\"",
                                "by",
                                book.getAuthor(),
                                "has comment(s):",
                                book.getComments().stream()
                                        .map(CommentDto::getContent)
                                        .collect(Collectors.joining(", "))
                        ))
                        .collect(Collectors.joining("\n")));
    }

    @ShellMethod(value = "Add book to the library", key = {"ab", "addBook"})
    @ShellMethodAvailability(value = "loggedIn")
    public String addBook(@ShellOption(defaultValue = "unnamed") String bookName,
                          @ShellOption(defaultValue = "people") String authorName,
                          @ShellOption(defaultValue = "general") String genreName) {
        libraryService.addBook(bookName, authorName, genreName);

        return "Book added";
    }

    @ShellMethod(value = "Add comment to the book", key = {"abc", "addBookComment"})
    @ShellMethodAvailability(value = "loggedIn")
    public String addBookComment(@ShellOption(defaultValue = "unnamed") String bookName,
                                 @ShellOption(defaultValue = "unnamed") String commentContent) {
        libraryService.addBookComment(bookName, commentContent);

        return "Comment added";
    }

    @ShellMethod(value = "Update book", key = {"ub", "updateBook"})
    @ShellMethodAvailability(value = "loggedIn")
    public String updateBook(@ShellOption(defaultValue = "unnamed") String oldBookName,
                             @ShellOption(defaultValue = "unnamed") String newBookName) {
        libraryService.updateBook(oldBookName, newBookName);

        return "Book updated";
    }

    @ShellMethod(value = "Update author", key = {"ua", "updateAuthor"})
    @ShellMethodAvailability(value = "loggedIn")
    public String updateAuthor(@ShellOption(defaultValue = "unnamed") String oldAuthorName,
                               @ShellOption(defaultValue = "unnamed") String newAuthorName) {
        libraryService.updateAuthor(oldAuthorName, newAuthorName);

        return "Author updated";
    }

    @ShellMethod(value = "Update genre", key = {"ug", "updateGenre"})
    @ShellMethodAvailability(value = "loggedIn")
    public String updateGenre(@ShellOption(defaultValue = "unnamed") String oldGenreName,
                              @ShellOption(defaultValue = "unnamed") String newGenreName) {
        libraryService.updateGenre(oldGenreName, newGenreName);

        return "Genre updated";
    }

    @ShellMethod(value = "Update comment of the book", key = {"ubc", "updateBookComment"})
    @ShellMethodAvailability(value = "loggedIn")
    public String updateBookComment(@ShellOption(defaultValue = "unnamed") String bookName,
                                    @ShellOption(defaultValue = "unnamed") String oldCommentContent,
                                    @ShellOption(defaultValue = "unnamed") String newCommentContent) {
        libraryService.updateBookComment(bookName, oldCommentContent, newCommentContent);

        return "Comment updated";
    }


    @ShellMethod(value = "Delete book from the library", key = {"db", "deleteBook"})
    @ShellMethodAvailability(value = "loggedIn")
    public String deleteBook(@ShellOption(defaultValue = "unnamed") String bookName) {
        libraryService.deleteBook(bookName);

        return "Book deleted";
    }

    @ShellMethod(value = "Delete comment of the book", key = {"dbc", "deleteBookComment"})
    @ShellMethodAvailability(value = "loggedIn")
    public String deleteBookComment(@ShellOption(defaultValue = "unnamed") String bookName,
                                    @ShellOption(defaultValue = "unnamed") String commentContent) {
        libraryService.deleteBookComment(bookName, commentContent);

        return "Comment deleted";
    }

    private Availability loggedIn() {
        return loginShellCommands.loggedIn();
    }
}
