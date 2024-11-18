package se3309a.library;

import javafx.beans.property.*;

import java.sql.Date;

public class HistoryLog {
    private IntegerProperty logID;
    private ObjectProperty<Date> generatedDate;
    private ObjectProperty<Staff> staff;
    private StringProperty text;
    private StringProperty type;

    public HistoryLog(){
        this.logID = new SimpleIntegerProperty();
        this.generatedDate = new SimpleObjectProperty();
        this.staff = new SimpleObjectProperty();
        this.text = new SimpleStringProperty();
        this.type = new SimpleStringProperty();
    }

    public void setStaff(Staff staff) {
        this.staff.set(staff);
    }

    public void setLogID(int logID) {
        this.logID.set(logID);
    }

    public void setGeneratedDate(Date generatedDate) {
        this.generatedDate.set(generatedDate);
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public Staff getStaff() {
        return staff.get();
    }

    public Date getGeneratedDate() {
        return generatedDate.get();
    }

    public int getLogID() {
        return logID.get();
    }

    public String getText() {
        return text.get();
    }

    public String getType() {
        return type.get();
    }

    public ObjectProperty<Date> generatedDateProperty() {
        return generatedDate;
    }

    public ObjectProperty<Staff> staffProperty() {
        return staff;
    }

    public IntegerProperty logIDProperty() {
        return logID;
    }

    public StringProperty textProperty() {
        return text;
    }

    public StringProperty typeProperty() {
        return type;
    }
}
