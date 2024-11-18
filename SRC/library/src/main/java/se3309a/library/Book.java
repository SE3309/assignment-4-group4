package se3309a.library;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Date;

public class Book {

    private StringProperty ISBN;
    private StringProperty title;
    private ObjectProperty<Date> publishedDate;


    public Book() {

        this.ISBN = new SimpleStringProperty();
        this.title = new SimpleStringProperty();
        this.publishedDate = new SimpleObjectProperty<>();
    }

    public void setISBN(String _ISBN) {
        ISBN.set(_ISBN);
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate.set(publishedDate);
    }

    public String getISBN() {
        return ISBN.get();
    }

    public String getTitle() {
        return title.get();
    }

    public Date getPublishedDate() {
        return publishedDate.get();
    }

    public ObjectProperty<Date> publishedDateProperty() {
        return publishedDate;
    }

    public StringProperty ISBNProperty() {
        return ISBN;
    }

    public StringProperty titleProperty() {
        return title;
    }
}
