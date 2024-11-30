package se3309a.library;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StaffContact {
    private ObjectProperty<Staff> staff;
    private StringProperty name;

    public StaffContact(){
        this.staff = new SimpleObjectProperty<>();
        this.name = new SimpleStringProperty();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setStaff(Staff staff) {
        this.staff.set(staff);
    }

    public Staff getStaff() {
        return staff.get();
    }

    public String getName() {
        return name.get();
    }

    public ObjectProperty<Staff> staffProperty() {
        return staff;
    }

    public StringProperty nameProperty() {
        return name;
    }
}
