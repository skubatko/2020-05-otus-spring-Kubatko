package ru.skubatko.dev.otus.spring.hw20.api;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ru.skubatko.dev.otus.spring.hw20.dto.BookDto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@DisplayName("Ресурс работы с книгами")
@WebMvcTest(BooksResource.class)
class BooksResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LibraryService service;

    private String baseUrl = "/api/books";
    private BookDto book = new BookDto("testName", "testAuthor", "testGenre", "testComments");

    @DisplayName("должен возвращать статус OK и все ожидаемые книги когда выполняется запрос GET по пути /api/books")
    @Test
    void shouldReturnStatusOkAndAllExpectedBooksWhenPerformsGetOnApiBooks() throws Exception {
        List<BookDto> books = List.of(book);
        given(service.findAllBooks()).willReturn(books);

        mockMvc.perform(
                get(baseUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().json(objectMapper.writeValueAsString(books)));
    }

    @DisplayName("должен возвращать статус CREATED и ожидаемую книгу когда выполняется запрос POST по пути /api/books")
    @Test
    void shouldReturnStatusCreatedAndExpectedBookWhenPerformsPostOnApiBooks() throws Exception {
        mockMvc.perform(
                post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(book)));

        verify(service).addBook(book);
    }

    @DisplayName("должен возвращать статус OK и ожидаемую книгу когда выполняется запрос PUT по пути /api/books/{oldBookName}")
    @Test
    void shouldReturnStatusOkAndExpectedBookWhenPerformsPutOnApiBooksWithOldBookName() throws Exception {
        String oldBookName = "oldBookName";
        String url = baseUrl + "/%s";

        mockMvc.perform(
                put(String.format(url, oldBookName))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(book)));

        verify(service).updateBook(oldBookName, book.getName());
    }

    @DisplayName("должен возвращать статус NO_CONTENT и когда выполняется запрос DELETE по пути /api/books/{bookName}")
    @Test
    void shouldReturnStatusNoContentWhenPerformsDeleteOnApiBooks() throws Exception {
        String url = baseUrl + "/%s";

        mockMvc.perform(
                delete(String.format(url, book.getName())))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(service).deleteBook(book.getName());
    }
}
