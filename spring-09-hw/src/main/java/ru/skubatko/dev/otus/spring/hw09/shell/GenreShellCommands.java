package ru.skubatko.dev.otus.spring.hw09.shell;

import ru.skubatko.dev.otus.spring.hw09.domain.Genre;
import ru.skubatko.dev.otus.spring.hw09.service.GenreService;

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
public class GenreShellCommands {

    private final GenreService genreService;
    private final LoginShellCommands loginShellCommands;

    @ShellMethod(value = "Find genre by id", key = {"fg", "findGenre"})
    @ShellMethodAvailability(value = "loggedIn")
    public String findGenreById(@ShellOption(defaultValue = "0") String id) {
        Genre genre = genreService.findById(Long.parseLong(id));
        if (genre == null) {
            return String.format("Genre with id = %s not found", id);
        }
        return String.format("Genre: %s", genre.getName());
    }

    @ShellMethod(value = "Find all genres in the library", key = {"fag", "findAllGenres"})
    @ShellMethodAvailability(value = "loggedIn")
    public String findAllGenres() {
        return String.format("Available genres: %s",
                genreService.findAll().stream().map(Genre::getName).collect(Collectors.joining(", ")));
    }

    @ShellMethod(value = "Add genre to the library", key = {"ag", "addGenre"})
    @ShellMethodAvailability(value = "loggedIn")
    public String addGenre(@ShellOption(defaultValue = "unnamed") String name) {
        Genre genre = new Genre();
        genre.setName(name);
        genreService.save(genre);
        return String.format("Genre %s added", name);
    }

    @ShellMethod(value = "Update genre to the library", key = {"ug", "updateGenre"})
    @ShellMethodAvailability(value = "loggedIn")
    public String updateGenre(@ShellOption(defaultValue = "0") String idString,
                              @ShellOption(defaultValue = "unnamed") String name) {
        long id = Long.parseLong(idString);
        Genre genre = genreService.findById(id);
        if (genre == null) {
            return String.format("Genre with id = %s cannot be found", idString);
        }

        genre.setName(name);
        genreService.update(genre);
        return String.format("Genre wih id = %s updated", idString);
    }

    @ShellMethod(value = "Delete genre by id", key = {"dg", "deleteGenre"})
    @ShellMethodAvailability(value = "loggedIn")
    public String deleteGenreById(@ShellOption(defaultValue = "0") String idString) {
        long id = Long.parseLong(idString);
        if (genreService.findById(id) == null) {
            return String.format("Genre with id = %s cannot be found", idString);
        }

        genreService.deleteById(id);
        return String.format("Genre with id = %s deleted", idString);
    }

    private Availability loggedIn() {
        return loginShellCommands.loggedIn();
    }
}
