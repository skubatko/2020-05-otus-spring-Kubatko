package ru.skubatko.dev.otus.spring.hw11.shell;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.CommandNotCurrentlyAvailable;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;

@DisplayName("Команды shell логина/логаута должны")
@SpringBootTest
class LoginShellCommandsTest {

    @Autowired
    private Shell shell;

    private static final String DEFAULT_LOGIN = "unnamed";
    private static final String LOGIN_COMMAND = "l";
    private static final String LOGIN_WELCOME_MESSAGE = "Welcome to library, %s!";
    private static final String LOGOUT_COMMAND = "bye";
    private static final String LOGOUT_GOODBYE_MESSAGE = "Goodbye, %s!";

    @DisplayName("возвращать приветствие для всех вариантов команды логина")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldReturnExpectedGreetingAfterLoginCommandEvaluated() {
        String actual = (String) shell.evaluate(() -> LOGIN_COMMAND);
        assertThat(actual).isEqualTo(String.format(LOGIN_WELCOME_MESSAGE, DEFAULT_LOGIN));

        shell.evaluate(() -> LOGOUT_COMMAND);

        actual = (String) shell.evaluate(() -> "login");
        assertThat(actual).isEqualTo(String.format(LOGIN_WELCOME_MESSAGE, DEFAULT_LOGIN));
    }

    @DisplayName("возвращать CommandNotCurrentlyAvailable при повторном логине")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldReturnCommandNotCurrentlyAvailableWhenUserAlreadyLoggedInAfterLoginCommandEvaluated() {
        shell.evaluate(() -> LOGIN_COMMAND);

        Object actual = shell.evaluate(() -> LOGIN_COMMAND);
        assertThat(actual).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }

    @DisplayName("возвращать прощание для всех вариантов команды логаута")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldReturnExpectedGoodbyeAfterLogoutCommandEvaluated() {
        assertLogout(LOGOUT_COMMAND);
        assertLogout("logout");
    }

    private void assertLogout(String logoutCommand) {
        shell.evaluate(() -> LOGIN_COMMAND);

        String actual = (String) shell.evaluate(() -> logoutCommand);
        assertThat(actual).isEqualTo(String.format(LOGOUT_GOODBYE_MESSAGE, DEFAULT_LOGIN));
    }

    @DisplayName("возвращать CommandNotCurrentlyAvailable при логауте без логина")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldReturnCommandNotCurrentlyAvailableWhenUserNotLoggedInAfterLogoutCommandEvaluated() {
        shell.evaluate(() -> LOGOUT_COMMAND);

        Object actual = shell.evaluate(() -> LOGOUT_COMMAND);
        assertThat(actual).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }
}
