package ru.skubatko.dev.otus.spring.hw05.shell;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ru.skubatko.dev.otus.spring.hw05.controller.QuizController;
import ru.skubatko.dev.otus.spring.hw05.enums.Mark;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.CommandNotCurrentlyAvailable;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;

@DisplayName("Тест команд shell")
@SpringBootTest
class AppShellCommandsTest {

    @MockBean
    private QuizController controller;

    @Autowired
    private Shell shell;

    private static final String COMMAND_PATTERN = "%s %s";
    private static final String TEST_USER = "testName";
    private static final String DEFAULT_LOGIN = "undefined";
    private static final String LOGIN_COMMAND = "l";
    private static final String LOGIN_WELCOME_MESSAGE = "Welcome, %s!";
    private static final String LOGOUT_COMMAND = "o";
    private static final String LOGOUT_GOODBYE_MESSAGE = "See you again, %s!";
    private static final String QUIZ_COMMAND = "qz";
    private static final String QUIZ_MESSAGE = "%s, your result is: %s";

    @DisplayName("должен возвращать приветствие для всех вариантов команды логина")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldReturnExpectedGreetingAfterLoginCommandEvaluated() {
        String actual = (String) shell.evaluate(() -> LOGIN_COMMAND);
        assertThat(actual).isEqualTo(String.format(LOGIN_WELCOME_MESSAGE, DEFAULT_LOGIN));

        shell.evaluate(() -> LOGOUT_COMMAND);

        actual = (String) shell.evaluate(() -> "login");
        assertThat(actual).isEqualTo(String.format(LOGIN_WELCOME_MESSAGE, DEFAULT_LOGIN));

        shell.evaluate(() -> LOGOUT_COMMAND);

        actual = (String) shell.evaluate(() -> String.format(COMMAND_PATTERN, "li", TEST_USER));
        assertThat(actual).isEqualTo(String.format(LOGIN_WELCOME_MESSAGE, TEST_USER));
    }

    @DisplayName("должен возвращать CommandNotCurrentlyAvailable при повторном логине")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldReturnCommandNotCurrentlyAvailableWhenUserAlreadyLoggedInAfterLoginCommandEvaluated() {
        shell.evaluate(() -> LOGIN_COMMAND);

        Object actual = shell.evaluate(() -> LOGIN_COMMAND);
        assertThat(actual).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }

    @DisplayName("должен возвращать результаты тестирования при выполнении команды quiz после логина пользователя")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldReturnExpectedQuizResultMessageWhenUserAlreadyLoggedInAfterQuizCommandEvaluated() {
        Mark expected = Mark.C;
        given(controller.makeQuizzed(TEST_USER)).willReturn(expected);

        shell.evaluate(() -> String.format(COMMAND_PATTERN, LOGIN_COMMAND, TEST_USER));

        String actual = (String) shell.evaluate(() -> QUIZ_COMMAND);
        assertThat(actual).isEqualTo(String.format(QUIZ_MESSAGE, TEST_USER, expected));
    }

    @DisplayName("должен возвращать CommandNotCurrentlyAvailable при выполнении команды quiz до логина пользователя")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldReturnCommandNotCurrentlyAvailableWhenUserNotLoggedInAfterQuizCommandEvaluated() {
        Object actual = shell.evaluate(() -> QUIZ_COMMAND);
        assertThat(actual).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }

    @DisplayName("должен возвращать прощание для всех вариантов команды логаута")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldReturnExpectedGoodbyeAfterLogoutCommandEvaluated() {
        assertLogout(LOGOUT_COMMAND);
        assertLogout("lo");
        assertLogout("out");
        assertLogout("logout");
    }

    private void assertLogout(String logoutCommand) {
        shell.evaluate(() -> LOGIN_COMMAND);

        String actual = (String) shell.evaluate(() -> logoutCommand);
        assertThat(actual).isEqualTo(String.format(LOGOUT_GOODBYE_MESSAGE, DEFAULT_LOGIN));
    }

    @DisplayName("должен возвращать CommandNotCurrentlyAvailable при логауте без логина")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldReturnCommandNotCurrentlyAvailableWhenUserNotLoggedInAfterLogoutCommandEvaluated() {
        shell.evaluate(() -> LOGOUT_COMMAND);

        Object actual = shell.evaluate(() -> LOGOUT_COMMAND);
        assertThat(actual).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }
}
