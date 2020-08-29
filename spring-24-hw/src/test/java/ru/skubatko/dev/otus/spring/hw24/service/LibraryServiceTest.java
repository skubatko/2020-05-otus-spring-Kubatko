package ru.skubatko.dev.otus.spring.hw24.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import ru.skubatko.dev.otus.spring.hw24.dto.BookDto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@DisplayName("Сервис для работы с библиотекой")
@Transactional
@SpringBootTest
class LibraryServiceTest {

    @Autowired
    private LibraryService libraryService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @DisplayName("должен находить ожидаемую книгу по её имени")
    @WithMockUser(username = "authenticatedUser")
    @Test
    public void shouldFindExpectedBookByName() {
        String name = "testBook1";

        BookDto actual = libraryService.findBookByName(name);

        assertAll(
                () -> assertThat(actual).isNotNull().hasFieldOrPropertyWithValue("name", name),
                () -> assertThat(actual.getAuthor()).isEqualTo("testAuthor1"),
                () -> assertThat(actual.getGenre()).isEqualTo("testGenre1"),
                () -> assertThat(actual.getComments()).isEqualTo("testBookComment1")
        );
    }

    @DisplayName("должен находить все ожидаемые книги библиотеки")
    @WithMockUser(username = "authenticatedUser")
    @Test
    public void shouldFindAllExpectedBooks() {
        List<BookDto> expected = Arrays.asList(
                new BookDto("testBook1", "testAuthor1", "testGenre1", "testBookComment1"),
                new BookDto("testBook2", "testAuthor2", "testGenre3", "testBookComment2"),
                new BookDto("testBook3", "testAuthor2", "testGenre4", "testBookComment3"),
                new BookDto("testBook4", "testAuthor3", "testGenre3", "testBookComment4"),
                new BookDto("testBook5", "testAuthor3", "testGenre4", "testBookComment5"),
                new BookDto("testBook6", "testAuthor3", "testGenre2", "testBookComment6")
        );

        List<BookDto> actual = libraryService.findAllBooks();

        assertThat(actual).hasSize(expected.size()).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("должен находить ожидаемые книги указанного автора")
    @WithMockUser(username = "authenticatedUser")
    @Test
    public void shouldFindExpectedBooksByAuthor() {
        String author = "testAuthor2";
        List<BookDto> expected = Arrays.asList(
                new BookDto("testBook2", "testAuthor2", "testGenre3", "testBookComment2"),
                new BookDto("testBook3", "testAuthor2", "testGenre4", "testBookComment3")
        );

        List<BookDto> actual = libraryService.findBooksByAuthor(author);

        assertThat(actual).hasSize(expected.size()).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("должен находить ожидаемые книги указанного жанра")
    @WithMockUser(username = "authenticatedUser")
    @Test
    public void shouldFindExpectedBooksByGenre() {
        String genre = "testGenre4";
        List<BookDto> expected = Arrays.asList(
                new BookDto("testBook3", "testAuthor2", "testGenre4", "testBookComment3"),
                new BookDto("testBook5", "testAuthor3", "testGenre4", "testBookComment5")
        );

        List<BookDto> actual = libraryService.findBooksByGenre(genre);

        assertThat(actual).hasSize(expected.size()).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("должен добавлять книгу в библиотеку для пользователя с правами админа")
    @Test
    public void shouldAddBookToLibraryForAdminUser() {
        String bookName = "testBookName";
        String authorName = "testAuthorName";
        String genreName = "testGenreName";

        setAuthenticatedAsAdmin();

        libraryService.addBook(new BookDto(bookName, authorName, genreName));

        BookDto actual = libraryService.findBookByName(bookName);

        assertAll(
                () -> assertThat(actual)
                              .hasFieldOrPropertyWithValue("name", bookName)
                              .hasFieldOrPropertyWithValue("author", authorName)
                              .hasFieldOrPropertyWithValue("genre", genreName),
                () -> assertThat(actual.getComments()).hasSize(0)
        );
    }

    @DisplayName("должен отклонять добавление книги в библиотеку для пользователя без прав админа")
    @Test
    public void shouldDenyToAddBookToLibraryForNonAdminUser() {
        String bookName = "testBookName";
        String authorName = "testAuthorName";
        String genreName = "testGenreName";

        setAuthenticatedAsUser();

        assertThatThrownBy(() -> libraryService.addBook(new BookDto(bookName, authorName, genreName)))
                .isInstanceOf(AccessDeniedException.class);
    }

    @DisplayName("должен добавлять комментарий к заданной книге для пользователя с правами админа")
    @Test
    public void shouldAddCommentToGivenBookForAdminUser() {
        String bookName = "testBook6";
        String comment = "testComment";
        BookDto expected = new BookDto(bookName, "testAuthor3", "testGenre2", "testBookComment6, " + comment);

        setAuthenticatedAsAdmin();

        libraryService.addBookComment(bookName, comment);

        BookDto actual = libraryService.findBookByName(bookName);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("должен отклонять добавление комментария к заданной книге для пользователя без прав админа")
    @Test
    public void shouldDenyToAddCommentToGivenBookForNonAdminUser() {
        String bookName = "testBook";
        String comment = "testComment";

        setAuthenticatedAsUser();

        assertThatThrownBy(() -> libraryService.addBookComment(bookName, comment))
                .isInstanceOf(AccessDeniedException.class);
    }

    @DisplayName("должен обновлять книгу новым именем для пользователя с правами админа")
    @Test
    public void shouldUpdateGivenBookWithNewNameForAdminUser() {
        String oldBookName = "testBook4";
        String newBookName = "testBookUpdated";
        BookDto expected = new BookDto(newBookName, "testAuthor3", "testGenre3", "testBookComment4");

        setAuthenticatedAsAdmin();

        libraryService.updateBook(oldBookName, newBookName);

        assertAll(
                () -> assertThat(libraryService.findBookByName(oldBookName)).isNull(),
                () -> assertThat(libraryService.findBookByName(newBookName)).isNotNull().isEqualTo(expected)
        );
    }

    @DisplayName("должен отклонять обновление книги новым именем для пользователя без прав админа")
    @Test
    public void shouldDenyToUpdateGivenBookWithNewNameForNonAdminUser() {
        String oldBookName = "testBook4";
        String newBookName = "testBookUpdated";

        setAuthenticatedAsUser();

        assertThatThrownBy(() -> libraryService.updateBook(oldBookName, newBookName))
                .isInstanceOf(AccessDeniedException.class);
    }

    @DisplayName("должен обновлять автора новым именем для пользователя с правами админа")
    @Test
    public void shouldUpdateGivenAuthorWithNewNameForAdminUser() {
        String oldAuthorName = "testAuthor2";
        String newAuthorName = "testAuthorUpdated";

        setAuthenticatedAsAdmin();

        libraryService.updateAuthor(oldAuthorName, newAuthorName);

        List<BookDto> booksByOldAuthor = libraryService.findBooksByAuthor(oldAuthorName);
        List<BookDto> booksByNewAuthor = libraryService.findBooksByAuthor(newAuthorName);
        assertAll(
                () -> assertThat(booksByOldAuthor).isEmpty(),
                () -> assertThat(booksByNewAuthor).hasSize(2)
        );
    }

    @DisplayName("должен отклонять обновление автора новым именем для пользователя без прав админа")
    @Test
    public void shouldDenyToUpdateGivenAuthorWithNewNameForNonAdminUser() {
        String oldAuthorName = "testAuthor";
        String newAuthorName = "testAuthorUpdated";

        setAuthenticatedAsUser();

        assertThatThrownBy(() -> libraryService.updateAuthor(oldAuthorName, newAuthorName))
                .isInstanceOf(AccessDeniedException.class);
    }

    @DisplayName("должен обновлять жанр новым именем")
    @Test
    public void shouldUpdateGivenGenreWithNewName() {
        String oldGenreName = "testGenre4";
        String newGenreName = "testGenreUpdated";

        libraryService.updateGenre(oldGenreName, newGenreName);
        List<BookDto> booksByOldGenre = libraryService.findBooksByGenre(oldGenreName);
        List<BookDto> booksByNewGenre = libraryService.findBooksByGenre(newGenreName);
        assertAll(
                () -> assertThat(booksByOldGenre).isEmpty(),
                () -> assertThat(booksByNewGenre).hasSize(2)
        );
    }

    @DisplayName("должен обновлять заданный комментарий заданной книги новым контентом")
    @Test
    public void shouldUpdateGivenCommentOfGivenBookWithNewContent() {
        String bookName = "testBook5";
        String oldBookComment = "testBookComment5";
        String newBookComment = "testBookCommentUpdated";

        libraryService.updateBookComment(bookName, oldBookComment, newBookComment);

        String actual = libraryService.findBookByName(bookName).getComments();

        assertThat(actual).isEqualTo(newBookComment);
    }

    @DisplayName("должен удалять заданную книгу")
    @Test
    public void shouldDeleteGivenBook() {
        String bookName = "testBook3";

        libraryService.deleteBook(bookName);

        assertThat(libraryService.findBookByName(bookName)).isNull();
    }

    @DisplayName("должен удалять заданный комментарий заданной книги")
    @Test
    public void shouldDeleteGivenCommentOfGivenBook() {
        String bookName = "testBook2";
        String commentContent = "testBookComment2";

        libraryService.deleteBookComment(bookName, commentContent);

        assertThat(libraryService.findBookByName(bookName).getComments()).isEmpty();
    }

    private void setAuthenticatedAsAdmin() {
        UserDetails admin = userDetailsService.loadUserByUsername("admin");
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(admin, "pass"));
    }

    private void setAuthenticatedAsUser() {
        UserDetails admin = userDetailsService.loadUserByUsername("user");
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(admin, "pass"));
    }
}
