package se3309a.library;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Date;

public class BookAuthor {
    private StringProperty author;
    private ObjectProperty<Book> book;

    public BookAuthor() {

        this.author = new SimpleStringProperty();
        this.book = new SimpleObjectProperty<>();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public void setBook(Book book) {
        this.book.set(book);
    }

    public Book getBook() {
        return book.get();
    }

    public String getAuthor() {
        return author.get();
    }

    public ObjectProperty<Book> bookProperty() {
        return book;
    }

    public StringProperty authorProperty() {
        return author;
    }
}
