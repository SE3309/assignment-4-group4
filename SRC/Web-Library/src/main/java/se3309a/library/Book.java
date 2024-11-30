package se3309a.library;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Date;

public class Book {

    private StringProperty ISBN;
    private StringProperty title;
    private StringProperty author;
    private StringProperty genreType;
    private StringProperty genreDescription;
    private ObjectProperty<Date> publishedDate;


    public Book() {

        this.ISBN = new SimpleStringProperty();
        this.title = new SimpleStringProperty();
        this.genreType = new SimpleStringProperty();
        this.genreDescription = new SimpleStringProperty();
        this.author = new SimpleStringProperty();
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

    public void setGenreType(String genreType) {
        this.genreType.set(genreType);
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public void setGenreDescription(String genreDescription) {
        this.genreDescription.set(genreDescription);
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

    public String getGenreDescription() {
        return genreDescription.get();
    }

    public String getGenreType() {
        return genreType.get();
    }

    public String getAuthor() {
        return author.get();
    }

    public StringProperty authorProperty() {
        return author;
    }

    public StringProperty genreTypeProperty() {
        return genreType;
    }

    public StringProperty genreDescriptionProperty() {
        return genreDescription;
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
