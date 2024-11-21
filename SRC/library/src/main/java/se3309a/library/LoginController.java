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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
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


    @FXML
    public void login() {
        String enteredUser = user.getText().trim();
        String enteredPassword = password.getText().trim();

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword())) {

            // Check staff table
            String staffSql = "SELECT jobType FROM staff WHERE sEmail = ? AND sPassword = ?";
            PreparedStatement staffStatement = connection.prepareStatement(staffSql);
            staffStatement.setString(1, enteredUser);
            staffStatement.setString(2, enteredPassword);
            ResultSet staffResultSet = staffStatement.executeQuery();

            if (staffResultSet.next()) {
                String userRole = staffResultSet.getString("jobType");
                if ("admin".equalsIgnoreCase(userRole)) {
                    libraryController.enableAdminMenuItems();
                    libraryController.disableBorrowerMenuItems();
                } else {
                    libraryController.enableAdminMenuItems(); // Adjust based on staff type logic
                }
            } else {
                // Check borrower table
                String borrowerSql = "SELECT borrowerID FROM borrower WHERE bEmail = ? AND bPassword = ?";
                PreparedStatement borrowerStatement = connection.prepareStatement(borrowerSql);
                borrowerStatement.setString(1, enteredUser);
                borrowerStatement.setString(2, enteredPassword);
                ResultSet borrowerResultSet = borrowerStatement.executeQuery();

                if (borrowerResultSet.next()) {
                    libraryController.enableBorrowerMenuItems();
                    libraryController.disableAdminMenuItems();
                } else {
                    errorMsg.setText("Invalid credentials. Please try again.");
                    return;
                }
            }

            // Close the login window after successful login
            Stage stage = (Stage) saveBtn.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            errorMsg.setText("Database error: " + e.getMessage());
        }
    }


    private void closeLoginWindow() {
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

    private boolean authenticateUser(String username, String password) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword())) {

            // Query to check if the username exists and matches the password directly
            String sql = "SELECT sPassword FROM staff WHERE sEmail = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("sPassword");
                System.out.println("Stored password: " + storedPassword);
                System.out.println("Entered password: " + password);
                return password.equals(storedPassword); // Compare passwords
            } else {
                System.out.println("No user found with email: " + username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Authentication failed
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