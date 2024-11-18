package se3309a.library;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.sql.Date;

public class Fines {
    private IntegerProperty fineID;
    private ObjectProperty<Date> dueDate;
    private ObjectProperty<Date> datePaid;
    private ObjectProperty<Borrower> borrower;

    public Fines(){
        this.fineID = new SimpleIntegerProperty();
        this.dueDate = new SimpleObjectProperty<>();
        this.datePaid = new SimpleObjectProperty<>();
        this.borrower = new SimpleObjectProperty<>();
    }

    public void setBorrower(Borrower borrower) {
        this.borrower.set(borrower);
    }

    public void setFineID(int fineID) {
        this.fineID.set(fineID);
    }

    public void setDatePaid(Date datePaid) {
        this.datePaid.set(datePaid);
    }

    public void setDueDate(Date dueDate) {
        this.dueDate.set(dueDate);
    }

    public Date getDatePaid() {
        return datePaid.get();
    }

    public Borrower getBorrower() {
        return borrower.get();
    }

    public Date getDueDate() {
        return dueDate.get();
    }

    public int getFineID() {
        return fineID.get();
    }

    public IntegerProperty fineIDProperty() {
        return fineID;
    }

    public ObjectProperty<Borrower> borrowerProperty() {
        return borrower;
    }

    public ObjectProperty<Date> datePaidProperty() {
        return datePaid;
    }

    public ObjectProperty<Date> dueDateProperty() {
        return dueDate;
    }
}
