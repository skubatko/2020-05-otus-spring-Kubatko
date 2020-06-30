package ru.skubatko.dev.otus.spring.hw09.shell;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import ru.skubatko.dev.otus.spring.hw09.domain.Author;
import ru.skubatko.dev.otus.spring.hw09.domain.Book;
import ru.skubatko.dev.otus.spring.hw09.domain.Genre;
import ru.skubatko.dev.otus.spring.hw09.service.AuthorService;
import ru.skubatko.dev.otus.spring.hw09.service.BookService;
import ru.skubatko.dev.otus.spring.hw09.service.GenreService;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.CommandNotCurrentlyAvailable;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@DisplayName("Команды shell работы с книгами библиотеки должны")
@Transactional
@SpringBootTest
class BookShellCommandsTest {

    @MockBean
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;

    @Autowired
    private Shell shell;

    private static final String LOGIN_COMMAND = "l";
    private static final String FIND_BOOK_COMMAND = "fb";
    private static final String FIND_ALL_BOOKS_COMMAND = "fab";
    private static final String ADD_BOOK_COMMAND = "ab";
    private static final String UPDATE_BOOK_COMMAND = "ub";
    private static final String DELETE_BOOK_COMMAND = "db";
    private static final String COUNT_BOOKS_COMMAND = "cb";

    @DisplayName("возвращать CommandNotCurrentlyAvailable при запросе незалогиненного пользователя")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldReturnCommandNotCurrentlyAvailableWhenUserNotLoggedInAfterLogoutCommandEvaluated() {
        Object actual = shell.evaluate(() -> FIND_BOOK_COMMAND);
        assertThat(actual).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }

    @DisplayName("возвращать ожидаемую книгу по её id при выполнении команды fb после логина пользователя")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldReturnBookWhenUserAlreadyLoggedInAfterFindBookCommandEvaluated() {
        Author author = new Author(1, "testAuthor");
        Genre genre = new Genre(1, "testGenre");
        Book book = new Book(1, "testBook", 1, 1);

        given(bookService.findById(1L)).willReturn(book);
        given(authorService.findById(1L)).willReturn(author);
        given(genreService.findById(1L)).willReturn(genre);

        String expected = String.format("Book: %s \"%s\" by %s", genre.getName(), book.getName(), author.getName());

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> FIND_BOOK_COMMAND + " 1");

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("возвращать все книги библиотеки при выполнении команды fab после логина пользователя")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldReturnAllBooksWhenUserAlreadyLoggedInAfterFindAllBooksCommandEvaluated() {
        Author author = new Author(1, "testAuthor");
        Genre genre = new Genre(1, "testGenre");
        Book book1 = new Book(1, "testBook1", 1L, 1L);
        Book book2 = new Book(2, "testBook2", 1L, 1L);
        List<Book> books = Arrays.asList(book1, book2);
        Map<Long, Author> authors = Map.of(1L, author);
        Map<Long, Genre> genres = Map.of(1L, genre);

        given(bookService.findAll()).willReturn(books);
        given(authorService.findAll()).willReturn(Collections.singletonList(author));
        given(genreService.findAll()).willReturn(Collections.singletonList(genre));

        String expected = String.format("Available books: %n%s",
                books.stream()
                        .map(book -> String.join(
                                StringUtils.SPACE,
                                genres.get(book.getGenreId()).getName(),
                                "\"" + book.getName() + "\"",
                                "by",
                                authors.get(book.getAuthorId()).getName()
                        ))
                        .collect(Collectors.joining("\n")));

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> FIND_ALL_BOOKS_COMMAND);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("добавлять ожидаемую книгу при выполнении команды ab после логина пользователя")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldAddBookWhenUserAlreadyLoggedInAfterAddBookCommandEvaluated() {
        String bookName = "testBook";

        given(bookService.insert(any(Book.class))).willReturn(1);

        String expected = String.format("Book %s added successfully", bookName);

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> ADD_BOOK_COMMAND + " 1 " + bookName + " 1 1");

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("обновлять ожидаемую книгу при выполнении команды ub после логина пользователя")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldUpdateBookWhenUserAlreadyLoggedInAfterUpdateBookCommandEvaluated() {
        Book book = new Book(1, "testBook", 1, 1);
        String updatedBookName = "testBookUpdated";

        given(bookService.findById(1)).willReturn(book);
        given(bookService.update(any(Book.class))).willReturn(1);

        String expected = String.format("Book %s updated successfully", updatedBookName);

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> UPDATE_BOOK_COMMAND + " 1 " + updatedBookName);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("удалять ожидаемую книгу при выполнении команды db после логина пользователя")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldDeleteBookWhenUserAlreadyLoggedInAfterDeleteBookCommandEvaluated() {
        String bookName = "testBook";
        Book book = new Book(1, bookName, 1, 1);

        given(bookService.findById(1)).willReturn(book);
        given(bookService.deleteById(1L)).willReturn(1);

        String expected = String.format("Book %s deleted successfully", bookName);

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> DELETE_BOOK_COMMAND + " 1 ");

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("возвращать ожидаемое количество книг в библиотеке при выполнении команды cb после логина пользователя")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldCountBooksWhenUserAlreadyLoggedInAfterCountBooksCommandEvaluated() {
        given(bookService.count()).willReturn(5L);

        String expected = String.format("In the library now %d book(s)", bookService.count());

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> COUNT_BOOKS_COMMAND);

        assertThat(actual).isEqualTo(expected);
    }
}
