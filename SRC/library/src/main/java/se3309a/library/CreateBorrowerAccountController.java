package se3309a.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateBorrowerAccountController implements Initializable {
    @FXML
    private Button cancelBtn;

    @FXML
    private TextField email;

    @FXML
    private Label errorMsg;

    @FXML
    private TextField fullName;

    @FXML
    private PasswordField password1;

    @FXML
    private PasswordField password2;

    @FXML
    private Button saveBtn;


    @FXML
    private TextField userName;
    @FXML
    private ComboBox<String> id;
    private String loggedInUser;
    final ObservableList<String> data = FXCollections.observableArrayList();
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

    @FXML
    private void saveBtnAction() {
        saveBorrowerAccount();

    }
    public void saveBorrowerAccount() {
        try {
            // Validate inputs
            String borrowerEmail = email.getText().trim();
            String borrowerPassword1 = password1.getText().trim();
            String borrowerPassword2 = password2.getText().trim();

            if (borrowerEmail.isEmpty() || borrowerPassword1.isEmpty() || borrowerPassword2.isEmpty()) {
                displayAlert("All fields are required!");
                return;
            }

            if (!borrowerPassword1.equals(borrowerPassword2)) {
                displayAlert("Passwords do not match!");
                return;
            }

            // Check if the email already exists in the database
            BorrowerTableAdapter borrowerAdapter = (BorrowerTableAdapter) borrowerTable;
            if (borrowerAdapter.isEmailRegistered(borrowerEmail)) {
                displayAlert("Email has an existing account!");
                return;
            }

            // Create a Borrower object
            Borrower borrower = new Borrower();
            borrower.setbEmail(borrowerEmail);
            borrower.setbPassword(borrowerPassword1);
            borrower.setMembershipDate(new java.sql.Date(System.currentTimeMillis()));

            // Insert borrower into the database
            borrowerAdapter.insertBorrower(borrower);

            // Show success message
            displayAlert("Borrower account created successfully!");
            clearForm();
        } catch (SQLException ex) {
            displayAlert("Error saving borrower: " + ex.getMessage());
        }
    }


    private void clearForm() {
        email.clear();
        password1.clear();
        password2.clear();
    }


    public void cancel() {
        // Get current stage reference
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        // Close stage
        stage.close();
    }

    private void displayAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void clearErrorMsg() {
        errorMsg.setText("");
    }

    public void buildData() {
//        try {
//            data.addAll(employeeTable.getKeys());
//        } catch (SQLException ex) {
//            displayAlert("ERROR - employeeAdapter: " + ex.getMessage());
//        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        saveBtn.setOnAction(event -> saveBtnAction());
    }

//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
////        id.setItems(data);
//    }


}
