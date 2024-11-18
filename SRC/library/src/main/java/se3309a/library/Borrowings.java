package se3309a.library;

import javafx.beans.property.*;

import java.sql.Date;

public class Borrowings {
    private IntegerProperty borrowingID;
    private ObjectProperty<Date> borrowDate;
    private ObjectProperty<Date> returnDate;
    private StringProperty status;
    private ObjectProperty<Book> book;
    private ObjectProperty<Borrower> borrower;
    private ObjectProperty<Fines> fine;

    public Borrowings(){
        this.borrowingID = new SimpleIntegerProperty();
        this.borrowDate = new SimpleObjectProperty<>();
        this.returnDate = new SimpleObjectProperty<>();
        this.status = new SimpleStringProperty();
        this.book = new SimpleObjectProperty<>();
        this.borrower = new SimpleObjectProperty<>();
        this.fine = new SimpleObjectProperty<>();
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate.set(borrowDate);
    }

    public void setBorrowingID(int borrowingID) {
        this.borrowingID.set(borrowingID);
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate.set(returnDate);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public void setBorrower(Borrower borrower) {
        this.borrower.set(borrower);
    }

    public void setBook(Book book) {
        this.book.set(book);
    }

    public void setFine(Fines fine) {
        this.fine.set(fine);
    }

    public Date getBorrowDate() {
        return borrowDate.get();
    }

    public Date getReturnDate() {
        return returnDate.get();
    }

    public int getBorrowingID() {
        return borrowingID.get();
    }

    public String getStatus() {
        return status.get();
    }

    public Borrower getBorrower() {
        return borrower.get();
    }

    public Book getBook() {
        return book.get();
    }

    public Fines getFine() {
        return fine.get();
    }

    public IntegerProperty borrowingIDProperty() {
        return borrowingID;
    }

    public ObjectProperty<Borrower> borrowerProperty() {
        return borrower;
    }

    public ObjectProperty<Book> bookProperty() {
        return book;
    }

    public ObjectProperty<Date> borrowDateProperty() {
        return borrowDate;
    }

    public ObjectProperty<Date> returnDateProperty() {
        return returnDate;
    }

    public ObjectProperty<Fines> fineProperty() {
        return fine;
    }

    public StringProperty statusProperty() {
        return status;
    }
}
