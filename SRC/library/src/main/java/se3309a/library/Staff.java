package se3309a.library;

import javafx.beans.property.*;

import java.sql.Date;

public class Staff {
    private IntegerProperty staffID;
    private StringProperty sPassword;
    private StringProperty sEmail;
    private StringProperty jobType;


    public Staff() {
        this.staffID = new SimpleIntegerProperty();
        this.sPassword = new SimpleStringProperty();
        this.sEmail = new SimpleStringProperty();
        this.jobType = new SimpleStringProperty();
    }

    public void setJobType(String jobType) {
        this.jobType.set(jobType);
    }

    public void setsEmail(String sEmail) {
        this.sEmail.set(sEmail);
    }

    public void setStaffID(int staffID) {
        this.staffID.set(staffID);
    }

    public void setsPassword(String sPassword) {
        this.sPassword.set(sPassword);
    }

    public int getStaffID() {
        return staffID.get();
    }

    public String getJobType() {
        return jobType.get();
    }

    public String getsEmail() {
        return sEmail.get();
    }

    public String getsPassword() {
        return sPassword.get();
    }

    public IntegerProperty staffIDProperty() {
        return staffID;
    }

    public StringProperty jobTypeProperty() {
        return jobType;
    }

    public StringProperty sPasswordProperty() {
        return sPassword;
    }

    public StringProperty sEmailProperty() {
        return sEmail;
    }
}
