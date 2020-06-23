package ru.skubatko.dev.otus.spring.hw05.shell;

import ru.skubatko.dev.otus.spring.hw05.config.AppProps;
import ru.skubatko.dev.otus.spring.hw05.controller.QuizController;
import ru.skubatko.dev.otus.spring.hw05.enums.Mark;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class AppShellCommands {

    private final QuizController controller;
    private final AppProps props;
    private final MessageSource messageSource;

    private String userName;

    @ShellMethod(value = "Login command", key = {"l", "li", "login"})
    @ShellMethodAvailability(value = "hasAlreadyLoggedIn")
    public String login(@ShellOption(defaultValue = "undefined") String userName) {
        this.userName = userName;
        return messageSource.getMessage("login", new String[]{userName}, props.getLocale());
    }

    @ShellMethod(value = "Take a quiz command", key = {"qz", "quiz"})
    @ShellMethodAvailability(value = "isUserLoggedIn")
    public String makeQuizzed() {
        Mark mark = controller.makeQuizzed(userName);
        return messageSource.getMessage("result", new String[]{userName, mark.name()}, props.getLocale());
    }

    @ShellMethod(value = "Logout command", key = {"o", "lo", "out", "logout"})
    @ShellMethodAvailability(value = "isUserLoggedIn")
    public String logout() {
        String userName = this.userName;
        this.userName = StringUtils.EMPTY;
        return messageSource.getMessage("logout", new String[]{userName}, props.getLocale());
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
