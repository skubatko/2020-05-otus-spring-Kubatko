package ru.skubatko.dev.otus.spring.hw13.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import ru.skubatko.dev.otus.spring.hw13.dto.BookDto;
import ru.skubatko.dev.otus.spring.hw13.dto.CommentDto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@DisplayName("Сервис для работы с библиотекой")
@SpringBootTest
class LibraryServiceTest {

    @Autowired
    private LibraryService service;

    @DisplayName("должен находить ожидаемую книгу по её имени")
    @Test
    public void shouldFindExpectedBookByName() {
        String name = "testBook1";

        BookDto actual = service.findBookByName(name);

        assertAll(
                () -> assertThat(actual).isNotNull().hasFieldOrPropertyWithValue("name", name),
                () -> assertThat(actual.getAuthor()).isEqualTo("testAuthor1"),
                () -> assertThat(actual.getGenre()).isEqualTo("testGenre1"),
                () -> assertThat(actual.getComments())
                              .hasSize(1)
                              .element(0).hasFieldOrPropertyWithValue("content", "testBookComment1")
        );
    }

    @DisplayName("должен находить все ожидаемые книги библиотеки")
    @Test
    public void shouldFindAllExpectedBooks() {
        List<BookDto> expected = Arrays.asList(
                new BookDto("testBook1", "testAuthor1", "testGenre1",
                        Collections.singletonList(new CommentDto("testBookComment1"))),
                new BookDto("testBook2", "testAuthor2", "testGenre3",
                        Collections.singletonList(new CommentDto("testBookComment2"))),
                new BookDto("testBook3", "testAuthor2", "testGenre4",
                        Collections.singletonList(new CommentDto("testBookComment3"))),
                new BookDto("testBook4", "testAuthor3", "testGenre3",
                        Collections.singletonList(new CommentDto("testBookComment4"))),
                new BookDto("testBook5", "testAuthor3", "testGenre4",
                        Collections.singletonList(new CommentDto("testBookComment5"))),
                new BookDto("testBook6", "testAuthor3", "testGenre2",
                        Collections.singletonList(new CommentDto("testBookComment6")))
        );

        List<BookDto> actual = service.findAllBooks();

        assertThat(actual).hasSize(expected.size()).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("должен находить ожидаемые книги указанного автора")
    @Test
    public void shouldFindExpectedBooksByAuthor() {
        String author = "testAuthor2";
        List<BookDto> expected = Arrays.asList(
                new BookDto("testBook2", "testAuthor2", "testGenre3",
                        Collections.singletonList(new CommentDto("testBookComment2"))),
                new BookDto("testBook3", "testAuthor2", "testGenre4",
                        Collections.singletonList(new CommentDto("testBookComment3")))
        );

        List<BookDto> actual = service.findBooksByAuthor(author);

        assertThat(actual).hasSize(expected.size()).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("должен находить ожидаемые книги указанного жанра")
    @Test
    public void shouldFindExpectedBooksByGenre() {
        String genre = "testGenre4";
        List<BookDto> expected = Arrays.asList(
                new BookDto("testBook3", "testAuthor2", "testGenre4",
                        Collections.singletonList(new CommentDto("testBookComment3"))),
                new BookDto("testBook5", "testAuthor3", "testGenre4",
                        Collections.singletonList(new CommentDto("testBookComment5")))
        );

        List<BookDto> actual = service.findBooksByGenre(genre);

        assertThat(actual).hasSize(expected.size()).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("должен добавлять книгу в библиотеку")
    @Test
    public void shouldAddBookToLibrary() {
        String bookName = "testBookName";
        String authorName = "testAuthorName";
        String genreName = "testGenreName";

        service.addBook(bookName, authorName, genreName);

        BookDto actual = service.findBookByName(bookName);

        assertAll(
                () -> assertThat(actual)
                              .hasFieldOrPropertyWithValue("name", bookName)
                              .hasFieldOrPropertyWithValue("author", authorName)
                              .hasFieldOrPropertyWithValue("genre", genreName),
                () -> assertThat(actual.getComments()).hasSize(0)
        );
    }

    @DisplayName("должен добавлять комментарий к заданной книге")
    @Test
    public void shouldAddCommentToGivenBook() {
        String bookName = "testBook6";
        String comment = "testComment";
        BookDto expected = new BookDto(bookName, "testAuthor3", "testGenre2",
                Arrays.asList(new CommentDto("testBookComment6"), new CommentDto(comment)));

        service.addBookComment(bookName, comment);

        BookDto actual = service.findBookByName(bookName);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("должен обновлять книгу новым именем")
    @Test
    public void shouldUpdateGivenBookWithNewName() {
        String oldBookName = "testBook4";
        String newBookName = "testBookUpdated";
        BookDto expected = new BookDto(newBookName, "testAuthor3", "testGenre3",
                Collections.singletonList(new CommentDto("testBookComment4")));

        service.updateBook(oldBookName, newBookName);

        assertAll(
                () -> assertThat(service.findBookByName(oldBookName)).isNull(),
                () -> assertThat(service.findBookByName(newBookName)).isNotNull().isEqualTo(expected)
        );
    }

    @DisplayName("должен обновлять автора новым именем")
    @Test
    public void shouldUpdateGivenAuthorWithNewName() {
        String oldAuthorName = "testAuthor2";
        String newAuthorName = "testAuthorUpdated";

        service.updateAuthor(oldAuthorName, newAuthorName);
        List<BookDto> booksByOldAuthor = service.findBooksByAuthor(oldAuthorName);
        List<BookDto> booksByNewAuthor = service.findBooksByAuthor(newAuthorName);
        assertAll(
                () -> assertThat(booksByOldAuthor).isEmpty(),
                () -> assertThat(booksByNewAuthor).hasSize(2)
        );
    }

    @DisplayName("должен обновлять жанр новым именем")
    @Test
    public void shouldUpdateGivenGenreWithNewName() {
        String oldGenreName = "testGenre4";
        String newGenreName = "testGenreUpdated";

        service.updateGenre(oldGenreName, newGenreName);
        List<BookDto> booksByOldGenre = service.findBooksByGenre(oldGenreName);
        List<BookDto> booksByNewGenre = service.findBooksByGenre(newGenreName);
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

        service.updateBookComment(bookName, oldBookComment, newBookComment);

        List<CommentDto> actual = service.findBookByName(bookName).getComments();

        assertThat(actual).hasSize(1).element(0).hasFieldOrPropertyWithValue("content", newBookComment);
    }

    @DisplayName("должен удалять заданную книгу")
    @Test
    public void shouldDeleteGivenBook() {
        String bookName = "testBook3";

        service.deleteBook(bookName);

        assertThat(service.findBookByName(bookName)).isNull();
    }

    @DisplayName("должен удалять заданный комментарий заданной книги")
    @Test
    public void shouldDeleteGivenCommentOfGivenBook() {
        String bookName = "testBook2";
        String commentContent = "testBookComment2";

        service.deleteBookComment(bookName, commentContent);

        assertThat(service.findBookByName(bookName).getComments()).isEmpty();
    }
}
