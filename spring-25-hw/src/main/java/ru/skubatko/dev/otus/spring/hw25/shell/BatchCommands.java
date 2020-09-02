package ru.skubatko.dev.otus.spring.hw25.shell;


import static ru.skubatko.dev.otus.spring.hw25.config.JobConfig.TRANSFER_LIBRARY_JOB_NAME;

import ru.skubatko.dev.otus.spring.hw25.dto.BookDto;
import ru.skubatko.dev.otus.spring.hw25.model.nosql.NoSqlBook;
import ru.skubatko.dev.otus.spring.hw25.model.nosql.NoSqlComment;
import ru.skubatko.dev.otus.spring.hw25.repository.nosql.NoSqlBookRepository;
import ru.skubatko.dev.otus.spring.hw25.service.SqlLibraryService;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class BatchCommands {

    private final SqlLibraryService libraryService;
    private final NoSqlBookRepository noSqlBookRepository;
    private final JobExplorer jobExplorer;

    private static final String SPACE = " ";

    @ShellMethod(value = "getAllSqlBooks", key = "sql")
    public String getAllSqlBooks() {
        List<BookDto> books = libraryService.findAllBooks();

        return String.format("Available SQL books: %n%s",
                books.stream()
                        .map(book -> String.join(
                                SPACE,
                                book.getGenre(),
                                "\"" + book.getName() + "\"",
                                "by",
                                book.getAuthor(),
                                "has comment(s):",
                                book.getComments())
                        )
                        .collect(Collectors.joining("\n")));
    }

    @ShellMethod(value = "getAllNoSqlBooks", key = "nosql")
    public String getAllNoSqlBooks() {
        List<NoSqlBook> books = noSqlBookRepository.findAll();

        return String.format("Available NoSQL books: %n%s",
                books.stream()
                        .map(book -> String.join(
                                SPACE,
                                book.getGenre(),
                                "\"" + book.getName() + "\"",
                                "by",
                                book.getAuthor(),
                                "has comment(s):",
                                book.getComments().stream()
                                        .map(NoSqlComment::getContent)
                                        .collect(Collectors.joining(", "))
                        ))
                        .collect(Collectors.joining("\n")));
    }

    @ShellMethod(value = "showInfo", key = "i")
    public void showInfo() {
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(TRANSFER_LIBRARY_JOB_NAME));
    }
}
