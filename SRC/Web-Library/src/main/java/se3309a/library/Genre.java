package se3309a.library;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Genre {

    private StringProperty genreType;
    private StringProperty genreDescription;
    private IntegerProperty genreCount;

    public Genre(){
        this.genreType = new SimpleStringProperty();
        this.genreDescription = new SimpleStringProperty();
        this.genreCount = new SimpleIntegerProperty();
    }

    public void setGenreType(String genreType) {
        this.genreType.set(genreType);
    }

    public void setGenreDescription(String genreDescription) {
        this.genreDescription.set(genreDescription);
    }

    public void setGenreCount(int genreCount) {
        this.genreCount.set(genreCount);
    }

    public String getGenreType() {
        return genreType.get();
    }

    public String getGenreDescription() {
        return genreDescription.get();
    }

    public int getGenreCount() {
        return genreCount.get();
    }

    public StringProperty genreDescriptionProperty() {
        return genreDescription;
    }

    public StringProperty genreTypeProperty() {
        return genreType;
    }

    public IntegerProperty genreCountProperty() {
        return genreCount;
    }
}
