package se3309a.library;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class ViewBookHistoryController implements Initializable {
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

    @FXML
    private TableColumn<Borrowings, Integer> borrowerIDColumn;

    @FXML
    private TableColumn<Borrowings, Date> borrowDateColumn;

    @FXML
    private TableColumn<Borrowings, String> ISBNColumn;

    @FXML
    private TableColumn<Borrowings, Integer> borrowingIDColumn;

    @FXML
    private TableColumn<Borrowings, Integer> fineIDColumn;

    @FXML
    private TableView<Borrowings> bookHistoryTable;

    @FXML
    private TableColumn<Borrowings, Date> returnDateColumn;

    @FXML
    private TableColumn<Borrowings, String> statusColumn;

    @FXML
    private Button button;

    @FXML
    private TextField emailField;

    @FXML
    void onEmailEntered(ActionEvent event) throws SQLException {
        String enteredEmail = emailField.getText().trim();  // Get the email from the input field

        if (!enteredEmail.isEmpty()) {
            // Call the method to query the database for borrowerId based on the email
            int borrowerId = getBorrowerIdByEmail(enteredEmail);

            if (borrowerId != -1) {
                System.out.println("Borrower ID: " + borrowerId);

                // Query fines table using the borrowerId
                loadHistoryForBorrower(borrowerId);
            } else {
                System.out.println("No borrower found with the email: " + enteredEmail);
            }
        } else {
            System.out.println("Please enter an email.");
        }
    }

    Borrowings borrowings = new Borrowings();
    private ObservableList<Borrowings> borrowingsData = FXCollections.observableArrayList();

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

    private int getBorrowerIdByEmail(String email) throws SQLException {
        // Find the borrower using the email
        Borrower borrower = (Borrower) borrowerTable.findOneRecord(email);

        // Check if the borrower exists
        if (borrower == null) {
            System.out.println("No borrower found with email: " + email);
            return -1;  // Indicate that no borrowing ID is found
        }

        // Retrieve the borrowing ID for the borrower
        Borrowings borrowing = (Borrowings) borrowingsTable.findOneRecord(String.valueOf(borrower.getBorrowerID()));
        if (borrowing == null) {
            System.out.println("No borrowing history for borrower with email: " + email);
            return -1;  // Indicate no borrowing history
        }

        return borrower.getBorrowerID();  // Return the borrowing ID
    }
    private void loadHistoryForBorrower(int borrowingId) throws SQLException {
        borrowings = (Borrowings) borrowingsTable.findOneRecord(String.valueOf(borrowingId));

        if (borrowings == null || borrowings.getBorrowingID() <= 0) {
            borrowingsData.clear();
            displayAlert("No borrowings found!");

        } else {
            borrowingsData.clear();  // Clear existing data before adding new fines

            // Add the fine to the ObservableList
            borrowingsData.add(borrowings);

            // Print the fine information to the console using the getter methods
            System.out.println(
                    "Borrowing ID: " + borrowings.getBorrowingID() +
                    ", Borrow Date: " + borrowings.getBorrowDate() +
                    ", Return Date: " + borrowings.getReturnDate() +
                    ", Status: " + borrowings.getStatus() +
                    ", Borrower ID: " + borrowings.getBorrower().getBorrowerID() +
                    ", ISBN: " + borrowings.getBook().getISBN() +
                    ", Fine ID: " + borrowings.getFine().getFineID()
                    );

                    // Set the data for the TableView
            bookHistoryTable.setItems(borrowingsData);
        }
    }

    private void displayAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        borrowingIDColumn.setCellValueFactory(new PropertyValueFactory<Borrowings, Integer>("borrowingID"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<Borrowings, Date>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<Borrowings, Date>("returnDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Borrowings, String>("status"));
        borrowerIDColumn.setCellValueFactory(cellData -> {
            Borrowings borrowings = cellData.getValue();
            if (borrowings.getBorrower() != null) {
                return new SimpleIntegerProperty(borrowings.getBorrower().getBorrowerID()).asObject();
            } else {
                System.err.println("Borrower is null for Borrowing ID: " + borrowings.getBorrowingID());
                return new SimpleIntegerProperty(-1).asObject(); // Default value
            }

        });
        ISBNColumn.setCellValueFactory(cellData -> {
            Borrowings borrowings = cellData.getValue();
            if (borrowings.getBook() != null) {
                return new SimpleStringProperty(borrowings.getBook().getISBN()); // Return ISBN as a String
            } else {
                System.err.println("Book is null for Borrowing ID: " + borrowings.getBorrowingID());
                return new SimpleStringProperty("N/A"); // Default or error value
            }

        });

        fineIDColumn.setCellValueFactory(cellData -> {
            Borrowings borrowings = cellData.getValue();
            if (borrowings.getFine() != null) {
                return new SimpleIntegerProperty(borrowings.getFine().getFineID()).asObject();
            } else {
                System.err.println("Fine is null for Borrowing ID: " + borrowings.getBorrowingID());
                return new SimpleIntegerProperty(-1).asObject(); // Default or error value
            }
        });
        bookHistoryTable.setItems(FXCollections.observableArrayList());
    }

    ObservableList<Borrowings> observableList= FXCollections.observableArrayList(
            borrowings
    );
}


