package se3309a.library;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class BookBorrowings {
    private ObjectProperty<Book> book;
    private ObjectProperty<Borrowings> borrowings;

    public BookBorrowings(){
        this.book = new SimpleObjectProperty<>();
        this.borrowings = new SimpleObjectProperty<>();
    }

    public void setBook(Book book) {
        this.book.set(book);
    }

    public void setBorrowings(Borrowings borrowings) {
        this.borrowings.set(borrowings);
    }

    public Book getBook() {
        return book.get();
    }

    public Borrowings getBorrowings() {
        return borrowings.get();
    }

    public ObjectProperty<Book> bookProperty() {
        return book;
    }

    public ObjectProperty<Borrowings> borrowingsProperty() {
        return borrowings;
    }
}
