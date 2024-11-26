package se3309a.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class FindBookBorrowingHistoryController implements Initializable {

    // Database-related objects
    private Connection databaseConnection;
    private DataStore bookTable,bookAuthorTable, bookGenreTable, bookBorrowingsTable,staffTable,staffContactTable;
    private DataStore borrowerTable;
    private DataStore bBorrowingsTable;
    private DataStore borrowerContactTable;
    private DataStore genreTable;
    private DataStore borrowingsTable;
    private DataStore reviewsTable,historyLogTable,finesTable;

    private LibraryController libraryController;
    @FXML
    private Label timesBorrowedLabel, authorOrBookLabel, avgDurationLabel;

    @FXML
    private ComboBox<String> searchTypeCombo;

    @FXML
    private TableView<Book> bookTableList;

    @FXML
    private TableColumn<Book, String> titleColumn, isbnColumn,authorColumn;

    @FXML
    private TextField searchField;

    private ObservableList<Book> filteredBooks = FXCollections.observableArrayList(); // Filtered results

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
                System.out.println("Selected ISBN: " + newValue.getISBN()); // Debugging step
                updateTimesBorrowed(newValue.getISBN());
                updateAverageBorrowDuration(newValue.getISBN());
            } else {
                timesBorrowedLabel.setText("N/A ");
                avgDurationLabel.setText("N/A");
            }
        });
    }

    private void updateTimesBorrowed(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            timesBorrowedLabel.setText("N/A ");
            return;
        }

        try {
            String query = "SELECT COUNT(*) AS borrowCount FROM borrowings WHERE ISBN = ?";
            PreparedStatement stmt = databaseConnection.prepareStatement(query);
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int borrowCount = rs.getInt("borrowCount");
                System.out.println("Borrow Count for ISBN " + isbn + ": " + borrowCount); // Debug
                timesBorrowedLabel.setText("" + borrowCount);
            } else {
                timesBorrowedLabel.setText("0");
            }
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
            // SQL query to fetch all borrow durations
            String query = """
                SELECT DATEDIFF(returnDate, borrowDate) AS borrowDuration
                FROM borrowings
                WHERE ISBN = ? AND returnDate IS NOT NULL
                """;

            PreparedStatement stmt = databaseConnection.prepareStatement(query);
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();

            int totalDays = 0; // Total sum of borrow durations
            int borrowCount = 0; // Number of borrow instances

            // Iterate through results
            while (rs.next()) {
                int duration = rs.getInt("borrowDuration");
                totalDays += duration; // Accumulate total duration
                borrowCount++; // Increment borrow count
            }

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
            String query = "SELECT b.ISBN, b.title, a.author FROM book b JOIN bookAuthor a ON b.ISBN = a.ISBN";
            PreparedStatement stmt = databaseConnection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Book book = new Book();
                book.setISBN(rs.getString("ISBN")); // Ensure ISBN is set
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                filteredBooks.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchByAuthor(String authorName) {
        try {
            String query = "SELECT b.ISBN, b.title, a.author FROM book b JOIN bookAuthor a ON b.ISBN = a.ISBN WHERE LOWER(a.author) LIKE ?";
            PreparedStatement stmt = databaseConnection.prepareStatement(query);
            stmt.setString(1, "%" + authorName + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Book book = new Book();
                book.setISBN(rs.getString("ISBN")); // Ensure ISBN is set
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                filteredBooks.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchByTitle(String title) {
        try {
            String query = "SELECT b.ISBN, b.title, a.author FROM book b JOIN bookAuthor a ON b.ISBN = a.ISBN WHERE LOWER(b.title) LIKE ?";
            PreparedStatement stmt = databaseConnection.prepareStatement(query);
            stmt.setString(1, "%" + title + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Book book = new Book();
                book.setISBN(rs.getString("ISBN")); // Ensure ISBN is set
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                filteredBooks.add(book);
            }
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
