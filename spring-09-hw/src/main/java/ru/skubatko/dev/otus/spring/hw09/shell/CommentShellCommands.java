package ru.skubatko.dev.otus.spring.hw09.shell;

import ru.skubatko.dev.otus.spring.hw09.domain.Book;
import ru.skubatko.dev.otus.spring.hw09.domain.Comment;
import ru.skubatko.dev.otus.spring.hw09.service.BookService;
import ru.skubatko.dev.otus.spring.hw09.service.CommentService;

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
public class CommentShellCommands {

    private final BookService bookService;
    private final CommentService commentService;
    private final LoginShellCommands loginShellCommands;

    @ShellMethod(value = "Find book comment by id", key = {"fc", "findComment"})
    @ShellMethodAvailability(value = "loggedIn")
    public String findBookCommentById(@ShellOption(defaultValue = "0") String id) {
        Comment comment = commentService.findById(Long.parseLong(id));
        if (comment == null) {
            return String.format("Book comment with id = %s not found", id);
        }
        return String.format("Book comment: %s", comment.getContent());
    }

    @ShellMethod(value = "Find all book comments in the library", key = {"fac", "findAllComments"})
    @ShellMethodAvailability(value = "loggedIn")
    public String findAllBookComments() {
        return String.format("Book comments: %n%s",
                commentService.findAll().stream().map(Comment::getContent).collect(Collectors.joining("\n")));
    }

    @ShellMethod(value = "Add book comment", key = {"ac", "addComment"})
    @ShellMethodAvailability(value = "loggedIn")
    public String addBookComment(@ShellOption(defaultValue = "default comment") String content,
                                 @ShellOption(defaultValue = "0") String bookId) {
        Book book = bookService.findById(Long.parseLong(bookId));
        if (book == null) {
            return String.format("Book with id = %s not found", bookId);
        }

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setBook(book);
        commentService.save(comment);
        return String.format("Book comment %s added", content);
    }

    @ShellMethod(value = "Update bookComment to the library", key = {"uc", "updateComment"})
    @ShellMethodAvailability(value = "loggedIn")
    public String updateBookComment(@ShellOption(defaultValue = "0") String idString,
                                    @ShellOption(defaultValue = "default comment") String content) {
        long id = Long.parseLong(idString);
        Comment comment = commentService.findById(id);
        if (comment == null) {
            return String.format("Book comment with id = %s cannot be found", idString);
        }

        comment.setContent(content);
        commentService.update(comment);
        return String.format("Book comment with id = %s updated", idString);
    }

    @ShellMethod(value = "Delete bookComment by id", key = {"dc", "deleteComment"})
    @ShellMethodAvailability(value = "loggedIn")
    public String deleteBookCommentById(@ShellOption(defaultValue = "0") String idString) {
        long id = Long.parseLong(idString);
        if (commentService.findById(id) == null) {
            return String.format("Book comment with id = %s cannot be found", id);
        }

        commentService.deleteById(id);
        return String.format("Book comment with id = %s deleted", idString);
    }

    private Availability loggedIn() {
        return loginShellCommands.loggedIn();
    }
}
