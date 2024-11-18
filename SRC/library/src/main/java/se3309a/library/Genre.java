package se3309a.library;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Genre {

    private StringProperty genreType;
    private StringProperty genreDescription;

    public Genre(){
        this.genreType = new SimpleStringProperty();
        this.genreDescription = new SimpleStringProperty();
    }

    public void setGenreType(String genreType) {
        this.genreType.set(genreType);
    }

    public void setGenreDescription(String genreDescription) {
        this.genreDescription.set(genreDescription);
    }

    public String getGenreType() {
        return genreType.get();
    }

    public String getGenreDescription() {
        return genreDescription.get();
    }

    public StringProperty genreDescriptionProperty() {
        return genreDescription;
    }

    public StringProperty genreTypeProperty() {
        return genreType;
    }
}
