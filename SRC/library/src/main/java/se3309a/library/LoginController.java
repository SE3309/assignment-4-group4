package se3309a.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controls the login screen
 */
public class LoginController implements Initializable {
    @FXML
    private TextField user;
    @FXML
    private PasswordField password;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Label errorMsg;

    private DataStore bookTable;
    private DataStore bookAuthorTable;
    private DataStore bookGenreTable;
    private DataStore bookBorrowingsTable;
    private DataStore staffTable;
    private DataStore staffContactTable;
    private DataStore borrowerTable;
    private DataStore bBorrowingsTable;
    private DataStore borrowerContactTable;
    private DataStore genreTable;
    private DataStore borrowingsTable;
    private DataStore reviewsTable;
    private DataStore historyLogTable;
    private DataStore finesTable;
    private LibraryController libraryController;
    final ObservableList<String> data = FXCollections.observableArrayList();

    public void setLibraryController(LibraryController controller) {
        libraryController = controller;
    }

    public void setDataStore(DataStore book, DataStore bookAuthor, DataStore bookGenre, DataStore bookBorrowings,
                             DataStore staff, DataStore staffContact, DataStore borrower, DataStore bBorrowings,
                             DataStore borrowerContact, DataStore genre, DataStore borrowings, DataStore reviews,
                             DataStore historyLog, DataStore fines) {
        bookTable = book;
        bookAuthorTable = bookAuthor;
        bookGenreTable = bookGenre;
        bookBorrowingsTable = bookBorrowings;
        staffTable = staff;
        staffContactTable = staffContact;
        borrowerTable = borrower;
        bBorrowingsTable = bBorrowings;
        borrowerContactTable = borrowerContact;
        genreTable = genre;
        borrowingsTable = borrowings;
        reviewsTable = reviews;
        historyLogTable = historyLog;
        finesTable = fines;
    }
    public void cancel() {
        // Get current stage reference
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        // Close stage
        stage.close();
    }

    public void clearErrorMsg() {
        errorMsg.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorMsg.setText("");
    }
}