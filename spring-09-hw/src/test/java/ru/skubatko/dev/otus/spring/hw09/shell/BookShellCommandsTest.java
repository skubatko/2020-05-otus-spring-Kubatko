package ru.skubatko.dev.otus.spring.hw09.shell;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import ru.skubatko.dev.otus.spring.hw09.domain.Author;
import ru.skubatko.dev.otus.spring.hw09.domain.Book;
import ru.skubatko.dev.otus.spring.hw09.domain.Comment;
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
    private static final String FIND_BOOK_BY_NAME_COMMAND = "fbn";
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
        Comment comment = new Comment(1L, "testBookComment1", null);
        Book book = new Book(1, "testBook", author, genre, Collections.singletonList(comment));
        comment.setBook(book);

        given(bookService.findByIdWithComments(1L)).willReturn(book);
        given(authorService.findById(1L)).willReturn(author);
        given(genreService.findById(1L)).willReturn(genre);

        String expected = String.format("Book: %s \"%s\" by %s has comment(s): %s",
                genre.getName(), book.getName(), author.getName(), comment.getContent());

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> FIND_BOOK_COMMAND + " 1");

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("возвращать ожидаемую книгу по её имени при выполнении команды fbn после логина пользователя")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldReturnBookByNameWhenUserAlreadyLoggedInAfterFindBookCommandEvaluated() {
        Author author = new Author(1, "testAuthor");
        Genre genre = new Genre(1, "testGenre");
        String name = "testBook";
        Comment comment = new Comment(1L, "testBookComment1", null);
        Book book = new Book(1, "testBook", author, genre, Collections.singletonList(comment));
        comment.setBook(book);

        given(bookService.findByNameWithComments(name)).willReturn(book);
        given(authorService.findById(1L)).willReturn(author);
        given(genreService.findById(1L)).willReturn(genre);

        String expected = String.format("Book: %s \"%s\" by %s has comment(s): %s",
                genre.getName(), book.getName(), author.getName(), comment.getContent());

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> FIND_BOOK_BY_NAME_COMMAND + StringUtils.SPACE + name);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("возвращать все книги библиотеки при выполнении команды fab после логина пользователя")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldReturnAllBooksWhenUserAlreadyLoggedInAfterFindAllBooksCommandEvaluated() {
        Author author = new Author(1, "testAuthor");
        Genre genre = new Genre(1, "testGenre");
        Comment comment1 = new Comment(1L, "testBookComment1", null);
        Comment comment2 = new Comment(2L, "testBookComment2", null);
        Book book1 = new Book(1, "testBook1", author, genre, Collections.singletonList(comment1));
        Book book2 = new Book(2, "testBook2", author, genre, Collections.singletonList(comment2));
        comment1.setBook(book1);
        comment2.setBook(book2);
        List<Book> books = Arrays.asList(book1, book2);

        given(bookService.findAllWithComments()).willReturn(books);

        String expected = String.format("Available books: %n%s",
                books.stream()
                        .map(book -> String.join(
                                StringUtils.SPACE,
                                book.getGenre().getName(),
                                "\"" + book.getName() + "\"",
                                "by",
                                book.getAuthor().getName(),
                                "has comment(s):",
                                book.getComments().stream()
                                        .map(Comment::getContent)
                                        .collect(Collectors.joining(", "))
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
        String authorName = "testAuthor";
        String genreName = "testGenre";

        String expected = String.format("Book %s added", bookName);

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> ADD_BOOK_COMMAND + StringUtils.SPACE
                                                              + bookName + StringUtils.SPACE
                                                              + authorName + StringUtils.SPACE
                                                              + genreName);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("обновлять ожидаемую книгу при выполнении команды ub после логина пользователя")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldUpdateBookWhenUserAlreadyLoggedInAfterUpdateBookCommandEvaluated() {
        Author author = new Author(1, "testAuthor");
        Genre genre = new Genre(1, "testGenre");
        Book book = new Book(1, "testBook", author, genre, Collections.emptyList());
        String updatedBookName = "testBookUpdated";

        given(bookService.findById(1)).willReturn(book);

        String expected = String.format("Book with id = %s updated", 1);

        shell.evaluate(() -> LOGIN_COMMAND);
        String actual = (String) shell.evaluate(() -> UPDATE_BOOK_COMMAND + " 1 " + updatedBookName);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("удалять ожидаемую книгу при выполнении команды db после логина пользователя")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldDeleteBookWhenUserAlreadyLoggedInAfterDeleteBookCommandEvaluated() {
        Author author = new Author(1, "testAuthor");
        Genre genre = new Genre(1, "testGenre");
        String bookName = "testBook";
        Book book = new Book(1, bookName, author, genre, Collections.emptyList());

        given(bookService.findById(1)).willReturn(book);

        String expected = "Book with id = 1 deleted successfully";

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
