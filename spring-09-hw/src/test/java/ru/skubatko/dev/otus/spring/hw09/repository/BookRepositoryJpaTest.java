package ru.skubatko.dev.otus.spring.hw09.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ru.skubatko.dev.otus.spring.hw09.domain.Author;
import ru.skubatko.dev.otus.spring.hw09.domain.Book;
import ru.skubatko.dev.otus.spring.hw09.domain.BookComment;
import ru.skubatko.dev.otus.spring.hw09.domain.Genre;
import ru.skubatko.dev.otus.spring.hw09.repository.jpa.BookRepositoryJpa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@DisplayName("Репозиторий для работы с книгами должен")
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import(BookRepositoryJpa.class)
class BookRepositoryJpaTest {

    @Autowired
    private BookRepositoryJpa repository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("находить ожидаемую книгу по её id")
    @Test
    void shouldFindExpectedBookById() {
        String name = "testBook2";
        Author author = new Author(2, "testAuthor2");
        Genre genre = new Genre(3, "testGenre3");

        Book actual = repository.findById(2).orElse(null);

        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("name", name)
                .hasFieldOrPropertyWithValue("author", author)
                .hasFieldOrPropertyWithValue("genre", genre);
    }

    @DisplayName("находить ожидаемую книгу с комментариями по её id")
    @Test
    void shouldFindExpectedBookByIdWithComments() {
        Author author = new Author(2, "testAuthor2");
        Genre genre = new Genre(3, "testGenre3");
        String bookCommentContent = "testBookComment2";
        Book book = new Book(2, "testBook2", author, genre, null);

        Book actual = repository.findByIdWithComments(2);

        assertThat(actual).isNotNull().isEqualToIgnoringGivenFields(book, "bookComments");
        assertThat(actual.getBookComments()).hasSize(1);
        assertThat(actual.getBookComments().get(0).getContent()).isEqualTo(bookCommentContent);
    }

    @DisplayName("находить ожидаемую книгу по её имени")
    @Test
    void shouldFindExpectedBookByName() {
        String name = "testBook2";
        Author author = new Author(2, "testAuthor2");
        Genre genre = new Genre(3, "testGenre3");

        Book actual = repository.findByName(name);

        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("name", name)
                .hasFieldOrPropertyWithValue("author", author)
                .hasFieldOrPropertyWithValue("genre", genre);
    }

    @DisplayName("находить ожидаемую книгу с комментариями по её имени")
    @Test
    void shouldFindExpectedBookByNameWithComments() {
        Author author = new Author(2, "testAuthor2");
        Genre genre = new Genre(3, "testGenre3");
        String bookCommentContent = "testBookComment2";
        String name = "testBook2";
        Book book = new Book(2, name, author, genre, null);

        Book actual = repository.findByNameWithComments(name);

        assertThat(actual).isNotNull().isEqualToIgnoringGivenFields(book, "bookComments");
        assertThat(actual.getBookComments()).hasSize(1);
        assertThat(actual.getBookComments().get(0).getContent()).isEqualTo(bookCommentContent);
    }

    @DisplayName("находить все книги")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldFindAllBooks() {
        List<Book> books = repository.findAll();
        assertThat(books)
                .hasSize(6)
                .extracting("name")
                .containsOnlyOnce("testBook1", "testBook2", "testBook3", "testBook4", "testBook5", "testBook6");
    }

    @DisplayName("находить все книги с комментариями")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldFindAllBooksWithComments() {
        List<Book> books = repository.findAllWithComments();
        assertThat(books)
                .hasSize(6)
                .extracting("name")
                .containsOnlyOnce("testBook1", "testBook2", "testBook3", "testBook4", "testBook5", "testBook6");

        List<BookComment> comments =
                books.stream().flatMap(book -> book.getBookComments().stream()).collect(Collectors.toList());
        assertThat(comments)
                .hasSize(6)
                .extracting("content")
                .containsOnlyOnce("testBookComment1", "testBookComment2", "testBookComment3",
                        "testBookComment4", "testBookComment5", "testBookComment6");
    }

    @DisplayName("добавлять книгу в базу данных")
    @Transactional
    @Test
    void shouldAddBook() {
        Book book = new Book();

        String name = "testBook7";
        book.setName(name);

        book.setAuthor(em.find(Author.class, 2L));
        book.setGenre(em.find(Genre.class, 3L));

        repository.save(book);

        Book actual = repository.findByName(name);
        assertThat(actual).isEqualTo(book);
    }

    @DisplayName("обновлять книгу в базе данных")
    @Transactional
    @Test
    void shouldUpdateBook() {
        Book book = repository.findByName("testBook3");
        String updatedName = "testBook3Updated";
        book.setName(updatedName);
        repository.save(book);

        Book actual = repository.findById(3).orElse(null);
        assertThat(actual).hasFieldOrPropertyWithValue("name", updatedName);
    }

    @DisplayName("удалять книгу по заданному id из базы данных")
    @Transactional
    @Test
    void shouldDeleteBookById() {
        repository.deleteById(1L);

        List<Book> books = repository.findAll();
        assertThat(books).extracting("id").doesNotContain(1L);
        assertThat(em.find(BookComment.class, 1L)).isNull();
        assertThat(em.find(BookComment.class, 2L)).isNull();
        assertThat(em.find(BookComment.class, 3L)).isNull();
    }

    @DisplayName("возвращать ожидаемое количество книг в базе данных")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnExpectedBooksCount() {
        long actual = repository.count();
        assertThat(actual).isEqualTo(6L);
    }
}
