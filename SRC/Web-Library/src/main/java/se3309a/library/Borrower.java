package se3309a.library;

import javafx.beans.property.*;

import java.sql.Date;

public class Borrower {
    private IntegerProperty borrowerID;
    private ObjectProperty<Date> membershipDate;
    private StringProperty bPassword;
    private StringProperty bEmail;

    public Borrower() {
        this.borrowerID = new SimpleIntegerProperty();
        this.membershipDate = new SimpleObjectProperty<>();
        this.bPassword = new SimpleStringProperty();
        this.bEmail = new SimpleStringProperty();
    }

    public void setBorrowerID(int borrowerID) {
        this.borrowerID.set(borrowerID);
    }

    public void setbEmail(String bEmail) {
        this.bEmail.set(bEmail);
    }

    public void setMembershipDate(Date membershipDate) {
        this.membershipDate.set(membershipDate);
    }

    public void setbPassword(String bPassword) {
        this.bPassword.set(bPassword);
    }

    public Date getMembershipDate() {
        return membershipDate.get();
    }

    public int getBorrowerID() {
        return borrowerID.get();
    }

    public String getbEmail() {
        return bEmail.get();
    }

    public String getbPassword() {
        return bPassword.get();
    }

    public IntegerProperty borrowerIDProperty() {
        return borrowerID;
    }

    public ObjectProperty<Date> membershipDateProperty() {
        return membershipDate;
    }

    public StringProperty bEmailProperty() {
        return bEmail;
    }

    public StringProperty bPasswordProperty() {
        return bPassword;
    }
}
