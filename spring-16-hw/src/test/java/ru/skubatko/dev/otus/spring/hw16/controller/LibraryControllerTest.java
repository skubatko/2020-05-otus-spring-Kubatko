package ru.skubatko.dev.otus.spring.hw16.controller;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import ru.skubatko.dev.otus.spring.hw16.dto.BookDto;
import ru.skubatko.dev.otus.spring.hw16.service.LibraryService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@DisplayName("Контроллер библиотеки")
@WebMvcTest(LibraryController.class)
class LibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryService service;

    @DisplayName("должен возвращать страницу со списком ожидаемых книг когда выполняется запрос GET по пути /library/books")
    @Test
    void shouldReturnPageOfExpectedBooksWhenPerformedGetRequestOnLibraryBooksPath() throws Exception {
        String bookName = "testBookName";
        String author = "testAuthor";
        String genre = "testGenre";
        String comments = "testComments";
        BookDto book = new BookDto(bookName, author, genre, comments);
        given(service.findAllBooks()).willReturn(List.of(book));

        mockMvc.perform(get("/library/books"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Books")))
                .andExpect(content().string(containsString(bookName)))
                .andExpect(content().string(containsString(author)))
                .andExpect(content().string(containsString(genre)))
                .andExpect(content().string(containsString(comments)));
    }

    @DisplayName("должен возвращать страницу добавления новой книги когда выполняется запрос GET по пути /library/books/add")
    @Test
    void shouldReturnPageOfAddBookWhenPerformedGetRequestOnLibraryBooksAddPath() throws Exception {
        mockMvc.perform(get("/library/books/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", any(BookDto.class)))
                .andExpect(view().name("add-book"))
                .andExpect(content().string(containsString("Add Book")));
    }

    @DisplayName("должен перенаправлять на страницу успешного добавления ожидаемой книги когда выполняется запрос POST по пути /library/books/add")
    @Test
    void shouldRedirectToExpectedBookAddSuccessPageWhenPerformedPostRequestOnLibraryBooksAddPath() throws Exception {
        String bookName = "testBookName";
        String author = "testAuthor";
        String genre = "testGenre";
        BookDto book = new BookDto(bookName, author, genre);
        given(service.findAllBooks()).willReturn(List.of(book));

        mockMvc.perform(post("/library/books/add")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("name", bookName)
                                .param("author", author)
                                .param("genre", genre))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("book", hasProperty("name", equalTo(bookName))))
                .andExpect(flash().attribute("book", hasProperty("author", equalTo(author))))
                .andExpect(flash().attribute("book", hasProperty("genre", equalTo(genre))))
                .andExpect(redirectedUrl("/library/books/add/success"));

        verify(service).addBook(book);
    }

    @DisplayName("должен возвращать страницу успешного добавления ожидаемой книги когда выполняется запрос GET по пути /library/books/add/success с флеш-атрибутом book")
    @Test
    void shouldReturnPageOfSuccessfulAddExpectedBookWhenPerformedGetRequestOnLibraryBooksAddSuccessPathWithBookFlashAttr() throws Exception {
        mockMvc.perform(get("/library/books/add/success")
                                .flashAttr("book", mock(BookDto.class)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", any(BookDto.class)))
                .andExpect(view().name("add-book-success"))
                .andExpect(content().string(containsString("Book added")));
    }

    @DisplayName("должен перенаправлять на страницу списка книг когда выполняется запрос GET по пути /library/books/add/success")
    @Test
    void shouldRedirectToPageOfBooksListWhenPerformedGetRequestOnLibraryBooksAddSuccessPath() throws Exception {
        mockMvc.perform(get("/library/books/add/success"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/library/books"));
    }

}
