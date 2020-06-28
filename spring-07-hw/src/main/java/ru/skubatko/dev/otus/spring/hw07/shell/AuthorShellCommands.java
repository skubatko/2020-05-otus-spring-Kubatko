package ru.skubatko.dev.otus.spring.hw07.shell;

import ru.skubatko.dev.otus.spring.hw07.domain.Author;
import ru.skubatko.dev.otus.spring.hw07.service.AuthorService;

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
public class AuthorShellCommands {

    private final AuthorService authorService;
    private final LoginShellCommands loginShellCommands;

    @ShellMethod(value = "Find author by id", key = {"fa", "findAuthor"})
    @ShellMethodAvailability(value = "loggedIn")
    public String findAuthorById(@ShellOption(defaultValue = "0") String id) {
        Author author = authorService.findById(Long.parseLong(id));
        if (author == null) {
            return String.format("Author with id = %s not found", id);
        }
        return String.format("Author: %s", author.getName());
    }

    @ShellMethod(value = "Find all authors in the library", key = {"faa", "findAllAuthors"})
    @ShellMethodAvailability(value = "loggedIn")
    public String findAllAuthors() {
        return String.format("Available authors: %s",
                authorService.findAll().stream().map(Author::getName).collect(Collectors.joining(", ")));
    }

    @ShellMethod(value = "Add author to the library", key = {"aa", "addAuthor"})
    @ShellMethodAvailability(value = "loggedIn")
    public String addAuthor(@ShellOption(defaultValue = "0") String id,
                            @ShellOption(defaultValue = "unnamed") String name) {
        int response = authorService.insert(new Author(Long.parseLong(id), name));
        if (response != 1) {
            return "Author cannot be added";
        }

        return String.format("Author %s added successfully", name);
    }

    @ShellMethod(value = "Update author to the library", key = {"ua", "updateAuthor"})
    @ShellMethodAvailability(value = "loggedIn")
    public String updateAuthor(@ShellOption(defaultValue = "0") String id,
                               @ShellOption(defaultValue = "unnamed") String name) {
        int response = authorService.update(new Author(Long.parseLong(id), name));
        if (response != 1) {
            return "Author cannot be updated";
        }

        return String.format("Author %s updated successfully", name);
    }

    @ShellMethod(value = "Delete author by id", key = {"da", "deleteAuthor"})
    @ShellMethodAvailability(value = "loggedIn")
    public String deleteAuthorById(@ShellOption(defaultValue = "0") String idString) {
        long id = Long.parseLong(idString);
        String name = authorService.findById(id).getName();
        int response = authorService.deleteById(id);
        if (response != 1) {
            return "Author cannot be deleted";
        }

        return String.format("Author %s deleted successfully", name);
    }

    @ShellMethod(value = "Get number of authors in the library", key = {"ca", "countAuthors"})
    @ShellMethodAvailability(value = "loggedIn")
    public String countAuthors() {
        return String.format("In the library now %d author(s)", authorService.count());
    }

    private Availability loggedIn() {
        return loginShellCommands.loggedIn();
    }
}
