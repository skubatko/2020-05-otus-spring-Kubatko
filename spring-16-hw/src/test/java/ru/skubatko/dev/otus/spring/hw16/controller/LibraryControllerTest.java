package ru.skubatko.dev.otus.spring.hw16.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ru.skubatko.dev.otus.spring.hw16.dto.BookDto;
import ru.skubatko.dev.otus.spring.hw16.service.LibraryService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

}
