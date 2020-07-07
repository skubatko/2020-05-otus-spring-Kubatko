package ru.skubatko.dev.otus.spring.hw09.shell;

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
public class LoginShellCommands {

    private String userName;

    @ShellMethod(value = "Login command", key = {"l", "login"})
    @ShellMethodAvailability(value = "notLoggedIn")
    public String login(@ShellOption(defaultValue = "unnamed") String userName) {
        this.userName = userName;
        return String.format("Welcome to library, %s!", userName);
    }

    @ShellMethod(value = "Logout command", key = {"bye", "logout"})
    @ShellMethodAvailability(value = "loggedIn")
    public String logout() {
        String userName = this.userName;
        this.userName = StringUtils.EMPTY;
        return String.format("Goodbye, %s!", userName);
    }

    public Availability loggedIn() {
        return StringUtils.isBlank(userName)
               ? Availability.unavailable("login required")
               : Availability.available();
    }

    private Availability notLoggedIn() {
        return StringUtils.isBlank(userName)
               ? Availability.available()
               : Availability.unavailable("logout required");
    }
}
