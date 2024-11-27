package se3309a.library;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ResourceBundle;

public class FindBookBorrowingHistoryController implements Initializable {
    // Database-related objects
    @FXML
    private TableView<String> borrowerTableList; // Holds the borrower names
    @FXML
    private TableColumn<String, String> borrowerNameColumn; // Column for borrower names

    private ObservableList<String> borrowerNames = FXCollections.observableArrayList();

    private Connection databaseConnection;
    private DataStore bookTable, bookAuthorTable, bookGenreTable, bookBorrowingsTable, staffTable, staffContactTable;
    private DataStore borrowerTable,bBorrowingsTable,borrowerContactTable,genreTable,borrowingsTable,reviewsTable, historyLogTable, finesTable;

    private LibraryController libraryController;
    @FXML
    private Label timesBorrowedLabel, authorOrBookLabel, avgDurationLabel;

    @FXML
    private ComboBox<String> searchTypeCombo;

    @FXML
    private TableView<Object> bookTableList;

    @FXML
    private TableColumn<Book, String> titleColumn, isbnColumn, authorColumn;

    @FXML
    private TextField searchField;

    private ObservableList<Object> filteredBooks = FXCollections.observableArrayList(); // Filtered results

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate ComboBox with search criteria
        searchTypeCombo.setItems(FXCollections.observableArrayList("Author Name", "Book Title Name"));
        searchTypeCombo.setValue("Book Title Name"); // Default value

        // Add listener to ComboBox to update the label text
        searchTypeCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("Author Name".equals(newValue)) {
                authorOrBookLabel.setText("Type author name:");
            } else if ("Book Title Name".equals(newValue)) {
                authorOrBookLabel.setText("Type book title:");
            }
        });

        // Set up the TableView columns
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        isbnColumn.setCellValueFactory(cellData -> cellData.getValue().ISBNProperty());

        // Bind the filteredBooks list to the TableView
        bookTableList.setItems(filteredBooks);

        // Add listener for row selection
        bookTableList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Selected ISBN: " + ((Book) newValue).getISBN()); // Debugging step
                updateTimesBorrowed(((Book) newValue).getISBN());
                updateAverageBorrowDuration(((Book) newValue).getISBN());
            } else {
                timesBorrowedLabel.setText("N/A ");
                avgDurationLabel.setText("N/A");
            }
        });

        // Bind the names to the column
        borrowerNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        borrowerTableList.setItems(borrowerNames);

        // Add listener for book selection
        bookTableList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String selectedISBN = ((Book) newValue).getISBN();
                populateBorrowerNames(selectedISBN);
            } else {
                borrowerNames.clear();
            }
        });
    }

    private void populateBorrowerNames(String isbn) {
        if (databaseConnection == null) {
            System.err.println("Database connection is not initialized.");
            return;
        }

        borrowerNames.clear();

        try {
            // to retrieve borrower names , I will be cross-referencing borrower,borrowings and borrower contact
            // Get Borrower IDs for the Selected ISBN
            String borrowerIdQuery = "SELECT borrowerID FROM borrowings WHERE ISBN = ?";
            PreparedStatement borrowerIdStmt = databaseConnection.prepareStatement(borrowerIdQuery);
            borrowerIdStmt.setString(1, isbn);
            ResultSet borrowerIdResultSet = borrowerIdStmt.executeQuery();

            while (borrowerIdResultSet.next()) {
                int borrowerID = borrowerIdResultSet.getInt("borrowerID");

                // Get Email for the Borrower ID
                String emailQuery = "SELECT bEmail FROM borrower WHERE borrowerID = ?";
                PreparedStatement emailStmt = databaseConnection.prepareStatement(emailQuery);
                emailStmt.setInt(1, borrowerID);
                ResultSet emailResultSet = emailStmt.executeQuery();

                if (emailResultSet.next()) {
                    String email = emailResultSet.getString("bEmail");

                    // Get Name from BorrowerContact
                    String nameQuery = "SELECT bName FROM borrowerContact WHERE bEmail = ?";
                    PreparedStatement nameStmt = databaseConnection.prepareStatement(nameQuery);
                    nameStmt.setString(1, email);
                    ResultSet nameResultSet = nameStmt.executeQuery();

                    if (nameResultSet.next()) {
                        String name = nameResultSet.getString("bName");
                        borrowerNames.add(name); // Add to the observable list
                    }
                }
            }

            System.out.println("Total Borrower Names Loaded: " + borrowerNames.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTimesBorrowed(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            timesBorrowedLabel.setText("N/A ");
            return;
        }
        try {
            int borrowCount = (int) borrowingsTable.findOneRecord(isbn, "");

            System.out.println("Borrow Count for ISBN " + isbn + ": " + borrowCount); // Debug
            timesBorrowedLabel.setText("" + borrowCount);

        } catch (Exception e) {
            e.printStackTrace();
            timesBorrowedLabel.setText("Error retrieving data");
        }
    }

    private void updateAverageBorrowDuration(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            avgDurationLabel.setText("N/A");
            return;
        }

        try {
            int duration = (int) bBorrowingsTable.findOneRecord2(isbn);
            int totalDays = 0; // Total sum of borrow durations
            int borrowCount = 0; // Number of borrow instances

            totalDays += duration; // Accumulate total duration
            borrowCount++; // Increment borrow count

            if (borrowCount > 0) {
                double avgDuration = (double) totalDays / borrowCount; // Calculate average
                avgDurationLabel.setText(String.format("%.2f days", avgDuration));
            } else {
                avgDurationLabel.setText("0 days");
            }
        } catch (Exception e) {
            e.printStackTrace();
            avgDurationLabel.setText("Error retrieving data");
        }
    }


    @FXML
    private void searchBooks() {
        if (databaseConnection == null) {
            System.err.println("Database connection is not initialized.");
            return;
        }

        String searchType = searchTypeCombo.getValue(); // Get selected search type
        String searchText = searchField.getText().trim().toLowerCase(); // Get search text

        filteredBooks.clear(); // Clear the current filteredBooks list

        if (searchText.isEmpty()) {
            loadAllBooks();
        } else if ("Author Name".equals(searchType)) {
            searchByAuthor(searchText);
        } else if ("Book Title Name".equals(searchType)) {
            searchByTitle(searchText);
        }
    }

    private void loadAllBooks() {
        try {
            List<Object> list = bookBorrowingsTable.getAllRecords();
            filteredBooks = FXCollections.observableArrayList(list);
            bookTableList.setItems(filteredBooks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchByAuthor(String authorName) {
        try {
            List<Object> list = bookBorrowingsTable.getAllRecords("%" + authorName + "%");
            filteredBooks = FXCollections.observableArrayList(list);
            bookTableList.setItems(filteredBooks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchByTitle(String title) {
        try {
            List<Object> list = bookBorrowingsTable.getAllRecords("%" + title + "%", "", "");
            filteredBooks = FXCollections.observableArrayList(list);
            bookTableList.setItems(filteredBooks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDataStore(DataStore book, DataStore bookAuthor, DataStore bookGenre, DataStore bookBorrowings,
                             DataStore staff, DataStore staffContact, DataStore borrower, DataStore bBorrowings,
                             DataStore borrowerContact, DataStore genre, DataStore borrowings, DataStore reviews,
                             DataStore historyLog, DataStore fines) {
        this.bookTable = book;
        this.bookAuthorTable = bookAuthor;
        this.bookGenreTable = bookGenre;
        this.bookBorrowingsTable = bookBorrowings;
        this.staffTable = staff;
        this.staffContactTable = staffContact;
        this.borrowerTable = borrower;
        this.bBorrowingsTable = bBorrowings;
        this.borrowerContactTable = borrowerContact;
        this.genreTable = genre;
        this.borrowingsTable = borrowings;
        this.reviewsTable = reviews;
        this.historyLogTable = historyLog;
        this.finesTable = fines;
    }

    public void setLibraryController(LibraryController controller) {
        this.libraryController = controller;
    }

    public void setDatabaseConnection(Connection connection) {
        this.databaseConnection = connection;
    }
}
