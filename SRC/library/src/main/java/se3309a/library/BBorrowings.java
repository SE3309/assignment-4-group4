package se3309a.library;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class BBorrowings {
    private ObjectProperty<Borrower> borrower;
    private ObjectProperty<Borrowings> borrowings;

    public BBorrowings(){
        this.borrower = new SimpleObjectProperty<>();
        this.borrowings = new SimpleObjectProperty<>();
    }

    public void setBorrower(Borrower borrower) {
        this.borrower.set(borrower);
    }

    public void setBorrowings(Borrowings borrowings) {
        this.borrowings.set(borrowings);
    }

    public Borrower getBorrower() {
        return borrower.get();
    }

    public Borrowings getBorrowings() {
        return borrowings.get();
    }

    public ObjectProperty<Borrowings> borrowingsProperty() {
        return borrowings;
    }

    public ObjectProperty<Borrower> borrowerProperty() {
        return borrower;
    }
}
