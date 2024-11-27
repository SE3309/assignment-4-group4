package se3309a.library;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField newPassword1;

    @FXML
    private TextField newPassword2;

    @FXML
    private TextField oldPassword;

    @FXML
    private Button saveBtn;

    @FXML
    private Label username;

    @FXML
    private Label errorMsg;

    private String loggedInUser, userAccountName;
    LibraryController libraryController;

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
    private int borrowerID;

    // To get the data store names from the caller
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

    public void setLibraryController(LibraryController controller) {
        libraryController = controller;
        loggedInUser = controller.getUserFullname();
        borrowerID = controller.getBorrowerID();
        username.setText("Change password for " + loggedInUser);
    }

    public void changePassword() {
        errorMsg.setText("");
        try {
            // Get the user account information from database
            Borrower borrower = (Borrower) borrowerTable.findOneRecord2(String.valueOf(borrowerID));
            // check the old password
            if (borrower.getbPassword().equals(oldPassword.getText())) {
                // check if the two new password are identical
                if (newPassword1.getText().equals(newPassword2.getText())) {
                    borrower.setbPassword(newPassword1.getText());
                    try {
                        borrowerTable.updateRecord(borrower);
                        libraryController.displayAlert("Password Updated! Logging Out..");

                        // Get current stage reference
                        Stage stage = (Stage) cancelBtn.getScene().getWindow();
                        // Close stage
                        stage.close();
                        libraryController.logout();
                    } catch (SQLException e) {
                        libraryController.displayAlert("Update Borrower Account: " + e.getMessage());
                    }
                } else {
                    // wrong new password
                    errorMsg.setText("The new passwords do not match");
                }
            } else {
                // wrong password
                errorMsg.setText("Wrong old password");
            }
        } catch (SQLException ex) {
            libraryController.displayAlert("Find Borrower Account: " + ex.getMessage());
        }
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

    }
}
