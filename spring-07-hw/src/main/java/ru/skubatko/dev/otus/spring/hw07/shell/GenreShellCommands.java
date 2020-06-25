package ru.skubatko.dev.otus.spring.hw07.shell;

import ru.skubatko.dev.otus.spring.hw07.domain.Genre;
import ru.skubatko.dev.otus.spring.hw07.service.GenreService;

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

    @ShellMethod(value = "Find genre by id", key = {"fg", "findGenreById"})
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
    public String addGenre(@ShellOption(defaultValue = "0") String id,
                           @ShellOption(defaultValue = "unnamed") String name) {
        int response = genreService.insert(new Genre(Long.parseLong(id), name));
        if (response != 1) {
            return "Genre cannot be added";
        }

        return String.format("Genre %s added successfully", name);
    }

    @ShellMethod(value = "Update genre to the library", key = {"ug", "updateGenre"})
    @ShellMethodAvailability(value = "loggedIn")
    public String updateGenre(@ShellOption(defaultValue = "0") String id,
                              @ShellOption(defaultValue = "unnamed") String name) {
        int response = genreService.update(new Genre(Long.parseLong(id), name));
        if (response != 1) {
            return "Genre cannot be updated";
        }

        return String.format("Genre %s updated successfully", name);
    }

    @ShellMethod(value = "Delete genre by id", key = {"dg", "deleteGenreById"})
    @ShellMethodAvailability(value = "loggedIn")
    public String deleteGenreById(@ShellOption(defaultValue = "0") String idString) {
        long id = Long.parseLong(idString);
        String name = genreService.findById(id).getName();
        int response = genreService.deleteById(id);
        if (response != 1) {
            return "Genre cannot be deleted";
        }

        return String.format("Genre %s deleted successfully", name);
    }

    @ShellMethod(value = "Get number of genres in the library", key = {"cg", "countGenres"})
    @ShellMethodAvailability(value = "loggedIn")
    public String countGenres() {
        return String.format("In the library now %d genre(s)", genreService.count());
    }

    private Availability loggedIn() {
        return loginShellCommands.loggedIn();
    }
}
