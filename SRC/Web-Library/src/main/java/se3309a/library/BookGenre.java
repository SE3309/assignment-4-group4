package se3309a.library;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class BookGenre {

    private ObjectProperty<Book> book;
    private ObjectProperty<Genre> genre;

    public BookGenre() {
        this.book = new SimpleObjectProperty<>();
        this.genre = new SimpleObjectProperty<>();

    }

    public void setBook(Book book) {
        this.book.set(book);
    }

    public void setGenre(Genre genre) {
        this.genre.set(genre);
    }

    public Book getBook() {
        return book.get();
    }

    public Genre getGenre() {
        return genre.get();
    }

    public ObjectProperty<Book> bookProperty() {
        return book;
    }

    public ObjectProperty<Genre> genreProperty() {
        return genre;
    }
}
