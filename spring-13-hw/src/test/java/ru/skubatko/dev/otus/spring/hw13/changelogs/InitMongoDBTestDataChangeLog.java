package ru.skubatko.dev.otus.spring.hw13.changelogs;

import ru.skubatko.dev.otus.spring.hw13.domain.Book;
import ru.skubatko.dev.otus.spring.hw13.domain.Comment;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;

@ChangeLog(order = "999")
@SuppressWarnings("unused")
public class InitMongoDBTestDataChangeLog {

    private MongoTemplate template;

    @ChangeSet(order = "000", id = "dropDB", author = "skubatko", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initBooks", author = "skubatko", runAlways = true)
    public void initLibrary(MongoTemplate template) {
        this.template = template;

        addBook("testBook1", "testAuthor1", "testGenre1", "testBookComment1");
        addBook("testBook2", "testAuthor2", "testGenre3", "testBookComment2");
        addBook("testBook3", "testAuthor2", "testGenre4", "testBookComment3");
        addBook("testBook4", "testAuthor3", "testGenre3", "testBookComment4");
        addBook("testBook5", "testAuthor3", "testGenre4", "testBookComment5");
        addBook("testBook6", "testAuthor3", "testGenre2", "testBookComment6");
    }

    private void addBook(String bookName, String author, String genre, String commentContent) {
        Comment comment = new Comment(commentContent, bookName);
        template.save(comment);
        Book book = new Book(bookName, author, genre);
        book.setComments(Collections.singletonList(comment));
        template.save(book);
    }
}
