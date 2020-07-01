package ru.skubatko.dev.otus.spring.hw09.shell;

import ru.skubatko.dev.otus.spring.hw09.domain.BookComment;
import ru.skubatko.dev.otus.spring.hw09.service.BookCommentService;

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
public class BookCommentShellCommands {

    private final BookCommentService bookCommentService;
    private final LoginShellCommands loginShellCommands;

    @ShellMethod(value = "Find book comment by id", key = {"fbc", "findBookComment"})
    @ShellMethodAvailability(value = "loggedIn")
    public String findBookCommentById(@ShellOption(defaultValue = "0") String id) {
        BookComment bookComment = bookCommentService.findById(Long.parseLong(id));
        if (bookComment == null) {
            return String.format("Book comment with id = %s not found", id);
        }
        return String.format("Book comment: %s", bookComment.getContent());
    }

    @ShellMethod(value = "Find all book comments in the library", key = {"fabc", "findAllBookComments"})
    @ShellMethodAvailability(value = "loggedIn")
    public String findAllBookComments() {
        return String.format("Available book comments: %n%s",
                bookCommentService.findAll().stream().map(BookComment::getContent).collect(Collectors.joining("\n")));
    }

    @ShellMethod(value = "Add book comment", key = {"abc", "addBookComment"})
    @ShellMethodAvailability(value = "loggedIn")
    public String addBookComment(@ShellOption(defaultValue = "0") String id,
                                 @ShellOption(defaultValue = "default comment") String content) {
        bookCommentService.save(new BookComment(Long.parseLong(id), content));
        return String.format("Book comment %s added successfully", content);
    }

    @ShellMethod(value = "Update bookComment to the library", key = {"ubc", "updateBookComment"})
    @ShellMethodAvailability(value = "loggedIn")
    public String updateBookComment(@ShellOption(defaultValue = "0") String id,
                                    @ShellOption(defaultValue = "default comment") String content) {
        bookCommentService.update(new BookComment(Long.parseLong(id), content));
        return String.format("Book comment with id = %s updated successfully", id);
    }

    @ShellMethod(value = "Delete bookComment by id", key = {"dbc", "deleteBookComment"})
    @ShellMethodAvailability(value = "loggedIn")
    public String deleteBookCommentById(@ShellOption(defaultValue = "0") String idString) {
        long id = Long.parseLong(idString);
        bookCommentService.deleteById(id);
        return String.format("Book comment with id = %s deleted successfully", idString);
    }

    @ShellMethod(value = "Get number of bookComments in the library", key = {"cbc", "countBookComments"})
    @ShellMethodAvailability(value = "loggedIn")
    public String countBookComments() {
        return String.format("In the library now %d book comment(s)", bookCommentService.count());
    }

    private Availability loggedIn() {
        return loginShellCommands.loggedIn();
    }
}
