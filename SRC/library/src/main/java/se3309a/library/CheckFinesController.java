package se3309a.library;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.ResourceBundle;

public class CheckFinesController implements Initializable {
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
    private TableColumn<Fines, Integer> fineIdColumn;

    @FXML
    private TableColumn<Fines, Integer> borrowerIdColumn;

    @FXML
    private TableColumn<Fines, Date> dueDateColumn;

    @FXML
    private TableColumn<Fines, Date> datePaidColumn;

    @FXML
    private TableColumn<Fines, Double> fineAmountColumn;

    @FXML
    private TableView<Fines> fineTable;

    @FXML
    private TextField emailField;

    Fines fine = new Fines();

    private ObservableList<Fines> finesData = FXCollections.observableArrayList();

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
    public void onEmailEntered() throws SQLException {
        String enteredEmail = emailField.getText().trim();  // Get the email from the input field

        if (!enteredEmail.isEmpty()) {
            // Call the method to query the database for borrowerId based on the email
            int borrowerId = getBorrowerIdByEmail(enteredEmail);

            if (borrowerId != -1) {
                System.out.println("Borrower ID: " + borrowerId);

                // Query fines table using the borrowerId
                loadFinesForBorrower(borrowerId);
            } else {
                System.out.println("No borrower found with the email: " + enteredEmail);
            }
        } else {
            System.out.println("Please enter an email.");
        }
    }

    private void loadFinesForBorrower(int borrowerId) throws SQLException {
        fine = (Fines) finesTable.findOneRecord(String.valueOf(borrowerId));

        if (fine == null || fine.getFineID() <= 0) {
            finesData.clear();
            displayAlert("No fines found!");

        } else{
            finesData.clear();  // Clear existing data before adding new fines

            // Add the fine to the ObservableList
            finesData.add(fine);

            // Print the fine information to the console using the getter methods
            System.out.println("Fine ID: " + fine.getFineID() +
                    ", Borrower ID: " + fine.getBorrower().getBorrowerID() +
                    ", Due Date: " + fine.getDueDate() +
                    ", Date Paid: " + fine.getDatePaid());

            // Set the data for the TableView
            fineTable.setItems(finesData);
        }
    }

    private void displayAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to query the borrowerId from the borrower table (already defined)
    private int getBorrowerIdByEmail(String email) throws SQLException {
      Borrower borrower = (Borrower) borrowerTable.findOneRecord(email);
      return borrower.getBorrowerID();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fineIdColumn.setCellValueFactory(new PropertyValueFactory<Fines, Integer>("fineID"));
        borrowerIdColumn.setCellValueFactory(cellData -> {
            Fines fine = cellData.getValue();
            if (fine.getBorrower() != null) {
                return new SimpleIntegerProperty(fine.getBorrower().getBorrowerID()).asObject();
            } else {
                System.err.println("Borrower is null for Fine ID: " + fine.getFineID());
                return new SimpleIntegerProperty(-1).asObject(); // Default or error value
            }
        });
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<Fines, Date>("dueDate"));
        datePaidColumn.setCellValueFactory(new PropertyValueFactory<Fines, Date>("datePaid"));

        fineAmountColumn.setCellValueFactory(cellData -> {
            Fines fine = cellData.getValue();
            Date dueDate = fine.getDueDate();
            Date datePaid = fine.getDatePaid();
            double fineAmount = 0.0;

            if (dueDate != null) {
                long overdueDays;
                if (datePaid != null) {
                    overdueDays = (datePaid.getTime() - dueDate.getTime()) / (1000 * 60 * 60 * 24);
                } else {
                    overdueDays = (System.currentTimeMillis() - dueDate.getTime()) / (1000 * 60 * 60 * 24);
                }
                if (overdueDays > 0) {
                    fineAmount = overdueDays * 0.50; // 50 cents per day
                }
            }
            return new SimpleDoubleProperty(fineAmount).asObject();
        });

        fineTable.setItems(FXCollections.observableArrayList());
    }

    ObservableList<Fines> observableList= FXCollections.observableArrayList(
          fine
    );
}
