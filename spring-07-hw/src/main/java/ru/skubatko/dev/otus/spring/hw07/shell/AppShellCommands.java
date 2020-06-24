package ru.skubatko.dev.otus.spring.hw07.shell;

import ru.skubatko.dev.otus.spring.hw07.service.AuthorService;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class AppShellCommands {

    private final AuthorService authorService;
    private String userName;

    @ShellMethod(value = "Login command", key = {"l", "login"})
    @ShellMethodAvailability(value = "hasAlreadyLoggedIn")
    public String login(@ShellOption(defaultValue = "undefined") String userName) {
        this.userName = userName;
        return String.format("Welcome to library, %s!", userName);
    }

    @ShellMethod(value = "Get all authors in the library", key = {"gaa", "getAllAuthors"})
    @ShellMethodAvailability(value = "isUserLoggedIn")
    public String getAllAuthors() {
        return String.format("In the library now %d authors",authorService.count());
    }

    @ShellMethod(value = "Logout command", key = {"bye", "logout"})
    @ShellMethodAvailability(value = "isUserLoggedIn")
    public String logout() {
        String userName = this.userName;
        this.userName = StringUtils.EMPTY;
        return String.format("Goodbye, %s", userName);
    }

    private Availability hasAlreadyLoggedIn() {
        return StringUtils.isBlank(userName)
               ? Availability.available()
               : Availability.unavailable("logout required");
    }

    private Availability isUserLoggedIn() {
        return StringUtils.isBlank(userName)
               ? Availability.unavailable("login required")
               : Availability.available();
    }
}
