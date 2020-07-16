package ru.skubatko.dev.otus.spring.hw13.shell;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ru.skubatko.dev.otus.spring.hw13.dto.BookDto;
import ru.skubatko.dev.otus.spring.hw13.dto.CommentDto;
import ru.skubatko.dev.otus.spring.hw13.service.LibraryService;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.CommandNotCurrentlyAvailable;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@DisplayName("Команды shell работы с библиотекой")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LibraryShellCommandsTest {

    @MockBean
    private LibraryService service;

    @Autowired
    private Shell shell;

    private static final String LOGIN_COMMAND = "l";
    private static final String FIND_BOOK_BY_NAME_COMMAND = "fbn";
    private static final String FIND_ALL_BOOKS_COMMAND = "fab";
    private static final String FIND_BOOKS_BY_AUTHOR_COMMAND = "fba";
    private static final String FIND_BOOKS_BY_GENRE_COMMAND = "fbg";
    private static final String ADD_BOOK_COMMAND = "ab";
    private static final String ADD_BOOK_COMMENT_COMMAND = "abc";
    private static final String UPDATE_BOOK_COMMAND = "ub";
    private static final String UPDATE_AUTHOR_COMMAND = "ua";
    private static final String UPDATE_GENRE_COMMAND = "ug";
    private static final String UPDATE_BOOK_COMMENT_COMMAND = "ubc";
    private static final String DELETE_BOOK_COMMAND = "db";
    private static final String DELETE_BOOK_COMMENT_COMMAND = "dbc";

    @DisplayName("должны возвращать CommandNotCurrentlyAvailable при запросе незалогиненного пользователя")
    @Test
    public void shouldReturnCommandNotCurrentlyAvailableWhenUserNotLoggedInAfterLogoutCommandEvaluated() {
        Object actual = shell.evaluate(() -> FIND_BOOK_BY_NAME_COMMAND);
        assertThat(actual).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }

    @DisplayName("должны возвращать ожидаемую книгу по её имени при выполнении команды fbn после логина пользователя")
    @Test
    public void shouldReturnBookByNameWhenUserAlreadyLoggedInAfterFindBookCommandEvaluated() {
        String bookName = "testBook";
        String author = "testAuthor";
        String genre = "testGenre";
        String comment = "testComment";
        BookDto book = new BookDto(bookName, author, genre, Collections.singletonList(new CommentDto(comment)));

        given(service.findBookByName(bookName)).willReturn(book);

        String expected = String.format("Book: %s \"%s\" by %s has comment(s): %s",
                genre, bookName, author, comment);

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> FIND_BOOK_BY_NAME_COMMAND + StringUtils.SPACE + bookName);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("должны возвращать все книги библиотеки при выполнении команды fab после логина пользователя")
    @Test
    public void shouldReturnAllBooksWhenUserAlreadyLoggedInAfterFindAllBooksCommandEvaluated() {
        BookDto book1 = new BookDto("testBook1", "testAuthor1", "testGenre",
                Collections.singletonList(new CommentDto("testComment1")));
        BookDto book2 = new BookDto("testBook2", "testAuthor2", "testGenre",
                Collections.singletonList(new CommentDto("testComment2")));
        List<BookDto> books = Arrays.asList(book1, book2);

        given(service.findAllBooks()).willReturn(books);

        String expected = String.format("Available books: %n%s",
                books.stream()
                        .map(book -> String.join(
                                StringUtils.SPACE,
                                book.getGenre(),
                                "\"" + book.getName() + "\"",
                                "by",
                                book.getAuthor(),
                                "has comment(s):",
                                book.getComments().stream()
                                        .map(CommentDto::getContent)
                                        .collect(Collectors.joining(", "))
                        ))
                        .collect(Collectors.joining("\n")));

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> FIND_ALL_BOOKS_COMMAND);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("должны возвращать книги заданного автора при выполнении команды fba после логина пользователя")
    @Test
    public void shouldReturnBooksByAuthorWhenUserAlreadyLoggedInAfterFindBooksByAuthorCommandEvaluated() {
        String author = "testAuthor";
        BookDto book1 = new BookDto("testBook1", author, "testGenre1",
                Collections.singletonList(new CommentDto("testComment1")));
        BookDto book2 = new BookDto("testBook2", author, "testGenre2",
                Collections.singletonList(new CommentDto("testComment2")));
        List<BookDto> books = Arrays.asList(book1, book2);

        given(service.findBooksByAuthor(author)).willReturn(books);

        String expected = String.format("Available books: %n%s",
                books.stream()
                        .map(book -> String.join(
                                StringUtils.SPACE,
                                book.getGenre(),
                                "\"" + book.getName() + "\"",
                                "by",
                                book.getAuthor(),
                                "has comment(s):",
                                book.getComments().stream()
                                        .map(CommentDto::getContent)
                                        .collect(Collectors.joining(", "))
                        ))
                        .collect(Collectors.joining("\n")));

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> FIND_BOOKS_BY_AUTHOR_COMMAND + StringUtils.SPACE + author);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("должны возвращать книги заданного жанра при выполнении команды fbg после логина пользователя")
    @Test
    public void shouldReturnBooksByGenreWhenUserAlreadyLoggedInAfterFindBooksByGenreCommandEvaluated() {
        String genre = "testGenre";
        BookDto book1 = new BookDto("testBook1", "testAuthor1", genre,
                Collections.singletonList(new CommentDto("testComment1")));
        BookDto book2 = new BookDto("testBook2", "testAuthor2", genre,
                Collections.singletonList(new CommentDto("testComment2")));
        List<BookDto> books = Arrays.asList(book1, book2);

        given(service.findBooksByGenre(genre)).willReturn(books);

        String expected = String.format("Available books: %n%s",
                books.stream()
                        .map(book -> String.join(
                                StringUtils.SPACE,
                                book.getGenre(),
                                "\"" + book.getName() + "\"",
                                "by",
                                book.getAuthor(),
                                "has comment(s):",
                                book.getComments().stream()
                                        .map(CommentDto::getContent)
                                        .collect(Collectors.joining(", "))
                        ))
                        .collect(Collectors.joining("\n")));

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> FIND_BOOKS_BY_GENRE_COMMAND + StringUtils.SPACE + genre);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("должны добавлять книгу при выполнении команды ab после логина пользователя")
    @Test
    public void shouldAddBookWhenUserAlreadyLoggedInAfterAddBookCommandEvaluated() {
        String expected = "Book added";

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> ADD_BOOK_COMMAND + StringUtils.SPACE
                                                              + "testBook" + StringUtils.SPACE
                                                              + "testAuthor" + StringUtils.SPACE
                                                              + "testGenre");
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("должны добавлять комментарий книги при выполнении команды abc после логина пользователя")
    @Test
    public void shouldAddBookCommentWhenUserAlreadyLoggedInAfterAddBookCommentCommandEvaluated() {
        String expected = "Comment added";

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> ADD_BOOK_COMMENT_COMMAND + StringUtils.SPACE
                                                              + "testBook" + StringUtils.SPACE
                                                              + "testComment");
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("должны обновлять книгу при выполнении команды ub после логина пользователя")
    @Test
    public void shouldUpdateBookWhenUserAlreadyLoggedInAfterUpdateBookCommandEvaluated() {
        String expected = "Book updated";

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> UPDATE_BOOK_COMMAND + StringUtils.SPACE
                                                              + "oldBookName" + StringUtils.SPACE
                                                              + "newBookName");
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("должны обновлять автора при выполнении команды ua после логина пользователя")
    @Test
    public void shouldUpdateAuthorWhenUserAlreadyLoggedInAfterUpdateAuthorCommandEvaluated() {
        String expected = "Author updated";

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> UPDATE_AUTHOR_COMMAND + StringUtils.SPACE
                                                              + "oldAuthorName" + StringUtils.SPACE
                                                              + "newAuthorName");
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("должны обновлять жанр при выполнении команды ug после логина пользователя")
    @Test
    public void shouldUpdateGenreWhenUserAlreadyLoggedInAfterUpdateGenreCommandEvaluated() {
        String expected = "Genre updated";

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> UPDATE_GENRE_COMMAND + StringUtils.SPACE
                                                              + "oldGenreName" + StringUtils.SPACE
                                                              + "newGenreName");
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("должны обновлять комментарий книги при выполнении команды ubc после логина пользователя")
    @Test
    public void shouldUpdateBookCommentWhenUserAlreadyLoggedInAfterUpdateBookCommentCommandEvaluated() {
        String expected = "Comment updated";

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> UPDATE_BOOK_COMMENT_COMMAND + StringUtils.SPACE
                                                              + "bookName" + StringUtils.SPACE
                                                              + "oldCommentContent" + StringUtils.SPACE
                                                              + "newCommentContent");
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("должны удалять книгу при выполнении команды db после логина пользователя")
    @Test
    public void shouldDeleteBookWhenUserAlreadyLoggedInAfterDeleteBookCommandEvaluated() {
        String expected = "Book deleted";

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> DELETE_BOOK_COMMAND + StringUtils.SPACE + "testBookName");

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("должны удалять комментарий книги при выполнении команды db после логина пользователя")
    @Test
    public void shouldDeleteBookCommentWhenUserAlreadyLoggedInAfterDeleteBookCommentCommandEvaluated() {
        String expected = "Comment deleted";

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> DELETE_BOOK_COMMENT_COMMAND + StringUtils.SPACE
                                                              + "testBookName" + StringUtils.SPACE
                                                              + "testBookComment");

        assertThat(actual).isEqualTo(expected);
    }
}
