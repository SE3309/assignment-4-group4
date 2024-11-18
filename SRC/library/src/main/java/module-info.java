module se2203b.ipayroll {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens se3309a.library to javafx.fxml;
    exports se3309a.library;
}