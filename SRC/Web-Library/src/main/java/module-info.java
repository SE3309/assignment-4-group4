/**
 * Module descriptor.
 *
 * @author Besmir Beqiri
 */
module one.jpro.hellojpro {
    requires javafx.controls;
    requires javafx.fxml;
    requires jpro.webapi;
    requires java.sql;

    exports se3309a.library;
    opens se3309a.library to javafx.fxml;
}