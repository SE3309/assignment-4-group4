package se3309a.library;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BorrowerContact {
    private ObjectProperty<Borrower> borrower;
    private StringProperty name;

    public BorrowerContact(){
        this.borrower = new SimpleObjectProperty<>();
        this.name = new SimpleStringProperty();
    }

    public void setBorrower(Borrower borrower) {
        this.borrower.set(borrower);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Borrower getBorrower() {
        return borrower.get();
    }

    public String getName() {
        return name.get();
    }

    public ObjectProperty<Borrower> borrowerProperty() {
        return borrower;
    }

    public StringProperty nameProperty() {
        return name;
    }
}
