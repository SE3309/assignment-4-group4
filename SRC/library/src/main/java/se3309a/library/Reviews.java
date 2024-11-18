package se3309a.library;

import javafx.beans.property.*;

import java.sql.Date;

public class Reviews {
    private IntegerProperty reviewID;
    private ObjectProperty<Date> reviewDate;
    private ObjectProperty<Borrower> borrower;
    private StringProperty reviewText;
    private IntegerProperty rating;
    private ObjectProperty<Book> book;

    public Reviews(){
        this.reviewID = new SimpleIntegerProperty();
        this.reviewDate = new SimpleObjectProperty();
        this.borrower = new SimpleObjectProperty();
        this.reviewText = new SimpleStringProperty();
        this.rating = new SimpleIntegerProperty();
        this.book = new SimpleObjectProperty();
    }

    public void setBorrower(Borrower borrower) {
        this.borrower.set(borrower);
    }

    public void setReviewID(int reviewID) {
        this.reviewID.set(reviewID);
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate.set(reviewDate);
    }

    public void setReviewText(String reviewText) {
        this.reviewText.set(reviewText);
    }

    public void setBook(Book book) {
        this.book.set(book);
    }

    public void setRating(int rating) {
        this.rating.set(rating);
    }

    public Borrower getBorrower() {
        return borrower.get();
    }

    public Date getReviewDate() {
        return reviewDate.get();
    }

    public int getReviewID() {
        return reviewID.get();
    }

    public String getReviewText() {
        return reviewText.get();
    }

    public Book getBook() {
        return book.get();
    }

    public int getRating() {
        return rating.get();
    }

    public ObjectProperty<Borrower> borrowerProperty() {
        return borrower;
    }

    public IntegerProperty reviewIDProperty() {
        return reviewID;
    }

    public ObjectProperty<Date> reviewDateProperty() {
        return reviewDate;
    }

    public StringProperty reviewTextProperty() {
        return reviewText;
    }

    public ObjectProperty<Book> bookProperty() {
        return book;
    }

    public IntegerProperty ratingProperty() {
        return rating;
    }
}
