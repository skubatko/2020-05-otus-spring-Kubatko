package ru.skubatko.dev.otus.spring.hw13.changelogs;

import ru.skubatko.dev.otus.spring.hw13.domain.Book;
import ru.skubatko.dev.otus.spring.hw13.domain.Comment;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;

@ChangeLog(order = "001")
@SuppressWarnings("unused")
public class InitMongoDBDataChangeLog {

    private MongoTemplate template;

    @ChangeSet(order = "000", id = "dropDB", author = "skubatko", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initBooks", author = "skubatko", runAlways = true)
    public void initLibrary(MongoTemplate template) {
        this.template = template;

        addBook("War and Peace", "Lev Tolstoy", "novel", "good novel");
        addBook("Resurrection", "Lev Tolstoy", "novel", "well written");
        addBook("The Three Hermits", "Lev Tolstoy", "poem", "classic");
        addBook("Ruslan and Ludmila", "Alexander Pushkin", "fable", "unbelievable");
        addBook("The Tale of the Fisherman and the Fish", "Alexander Pushkin", "fable", "good story");
        addBook("Snowstorm", "Alexander Pushkin", "novel", "epic");
        addBook("A Crow and a fox", "Ivan Krylov", "fable", "as always");
        addBook("Two dogs", "Ivan Krylov", "fable", "never mind");
        addBook("Mouses", "Ivan Krylov", "fable", "true story");
    }

    private void addBook(String bookName, String author, String genre, String commentContent) {
        Comment comment = new Comment(commentContent, bookName);
        template.save(comment);
        Book book = new Book(bookName, author, genre);
        book.setComments(Collections.singletonList(comment));
        template.save(book);
    }
}
