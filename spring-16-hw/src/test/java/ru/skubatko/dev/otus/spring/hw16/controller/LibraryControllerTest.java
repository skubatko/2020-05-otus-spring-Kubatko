package ru.skubatko.dev.otus.spring.hw16.controller;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.BDDMockito.given;
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
    public void shouldReturnPageOfExpectedBooksWhenPerformedGetRequestOnLibraryBooksPath() throws Exception {
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
    public void shouldReturnPageOfAddBookWhenPerformedGetRequestOnLibraryBooksAddPath() throws Exception {
        mockMvc.perform(get("/library/books/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", any(BookDto.class)))
                .andExpect(view().name("add-book"))
                .andExpect(content().string(containsString("Add Book")));
    }

    @DisplayName("должен возвращать страницу со списком ожидаемых книг когда выполняется запрос POST по пути /library/books")
    @Test
    public void shouldReturnPageOfExpectedBooksWhenPerformedPostRequestOnLibraryBooksPath() throws Exception {
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
                .andExpect(redirectedUrl("/library/books"));

        verify(service).addBook(book);
    }

}
