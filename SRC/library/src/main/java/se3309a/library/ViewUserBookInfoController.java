package se3309a.library;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;


public class ViewUserBookInfoController  {
    //table 1: declarations for viewing all users in database as well as how many books they have borrowed in total
    @FXML
    private ComboBox<String> criteriaCombo;
    @FXML
    private TextField searchField;

    @FXML
    private Label emailOrNameLabel;
    @FXML
    private TableView<Borrower> tableView;
    @FXML
    private TableColumn<Borrower, Number> idColumn,numOfBooksBorrowedColumn;
    @FXML
    private TableColumn<Borrower, String> nameColumn, emailColumn;

    // table 2: declarations for book history
    @FXML
    private TableColumn<Map<String, Object>, String> bookISBN; // ISBN as String
    @FXML
    private TableColumn<Map<String, Object>, String> bookTitleColumn; // Title as String
    @FXML
    private TableColumn<Map<String, Object>, Date> borrowDateColumn; // Borrow Date as Date
    @FXML
    private TableColumn<Map<String, Object>, Date> returnDateColumn; // Return Date as Date
    @FXML
    private TableView<Map<String, Object>> tableView2; // TableView holds Map rows
    @FXML
    private Label userNameLabel; // Displays user name


    private DataStore bookTable,bookAuthorTable,bookGenreTable,bookBorrowingsTable,staffTable,staffContactTable,borrowerTable,bBorrowingsTable,borrowerContactTable,genreTable,borrowingsTable,reviewsTable,historyLogTable,finesTable;
    // List to hold Borrower data
    private ObservableList<Borrower> borrowerList = FXCollections.observableArrayList();

    private LibraryController libraryController;
    final ObservableList<String> data = FXCollections.observableArrayList();
    private Map<String, String> borrowerContactMap = new HashMap<>();
    private Map<Integer, Integer> borrowCountMap = new HashMap<>();

    private ObservableList<BorrowerContact> borrowerContactList = FXCollections.observableArrayList();
    private Connection databaseConnection;


    public void initialize() {
        // Initialize the ComboBox with search criteria
        criteriaCombo.getItems().addAll("Name", "Email", "BorrowerID");
        criteriaCombo.setValue("BorrowerID"); //

        // Add listener to ComboBox to update the label text
        criteriaCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("Email".equals(newValue)) {
                emailOrNameLabel.setText("Type Borrower Email:");
            } else if ("Name".equals(newValue)) {
                emailOrNameLabel.setText("Type Borrower Name:");
            } else if ("BorrowerID".equals(newValue)) {
                emailOrNameLabel.setText("Type BorrowerID:");
            }
        });
        // Set up the TableView columns
        // Bind the ID column to the Borrower ID property
        idColumn.setCellValueFactory(cellData -> cellData.getValue().borrowerIDProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().bEmailProperty());

        // Cross-reference emails for the name column
        nameColumn.setCellValueFactory(cellData -> {
            String email = cellData.getValue().getbEmail();
            String name = borrowerContactMap.get(email); // Fetch name from map using email
            return new SimpleStringProperty(name != null ? name : "Unknown");
        });
        numOfBooksBorrowedColumn.setCellValueFactory(cellData -> {
            int borrowerID = cellData.getValue().getBorrowerID(); // Get Borrower ID
            int borrowCount = borrowCountMap.getOrDefault(borrowerID, 0); // Fetch count from map
            return new SimpleIntegerProperty(borrowCount); // Return as a property for binding
        });


        // Bind borrower list to the TableView
        tableView.setItems(borrowerList);

        // Load data
        loadBorrowerContactDataFromDatabase(); // Load BorrowerContact data first
        loadBorrowerDataFromDatabase();       // Load Borrower data
        preloadBorrowingCounts();

        // Add listener for table selection
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Fetch the name directly from the nameColumn for the selected row
                String name = nameColumn.getCellObservableValue(newValue).getValue();

                // Update the userNameLabel
                userNameLabel.setText(name);

                // Log for debugging
                System.out.println("Selected User Name: " + name);
            }
        });

        // tableView2 columns
        bookISBN.setCellValueFactory(cellData ->
                new SimpleStringProperty((String) cellData.getValue().get("ISBN")));

        bookTitleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty((String) cellData.getValue().get("Title")));

        borrowDateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>((Date) cellData.getValue().get("BorrowDate")));

        returnDateColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>((Date) cellData.getValue().get("ReturnDate")));

    }

    public void search(ActionEvent event) {
        String searchText = searchField.getText().trim().toLowerCase(); // Get the text from the search field
        String selectedCriteria = criteriaCombo.getValue(); // Get the selected criteria from the ComboBox

        if (searchText.isEmpty()) {
            System.out.println("Search field is empty. Showing all records.");
            tableView.setItems(borrowerList); // Reset to show all items
            return;
        }

        ObservableList<Borrower> filteredList = FXCollections.observableArrayList();

        for (Borrower borrower : borrowerList) {
            switch (selectedCriteria) {
                case "BorrowerID": // Search by ID
                    if (String.valueOf(borrower.getBorrowerID()).contains(searchText)) {
                        filteredList.add(borrower);
                    }
                    break;

                case "Name": // Search by Name
                    String name = borrowerContactMap.get(borrower.getbEmail());
                    if (name != null && name.toLowerCase().contains(searchText)) {
                        filteredList.add(borrower);
                    }
                    break;

                case "Email": // Search by Email
                    if (borrower.getbEmail().toLowerCase().contains(searchText)) {
                        filteredList.add(borrower);
                    }
                    break;

                default:
                    System.out.println("Invalid search criteria selected.");
            }
        }

        // Update the TableView with the filtered list
        tableView.setItems(filteredList);

        // Log the search results
        System.out.println("Search completed. Total matches found: " + filteredList.size());
    }
    public void viewUserBookHistory(ActionEvent event) {
        // Get selected borrower from tableView
        Borrower selectedBorrower = tableView.getSelectionModel().getSelectedItem();

        if (selectedBorrower == null) {
            System.out.println("No borrower selected.");
            return;
        }

        int borrowerID = selectedBorrower.getBorrowerID();

        // Clear existing data in tableView2
        tableView2.getItems().clear();

        // SQL queries
        String borrowingsQuery = "SELECT ISBN, borrowDate, returnDate FROM borrowings WHERE borrowerID = ?";
        String bookQuery = "SELECT title FROM book WHERE ISBN = ?";

        try (PreparedStatement borrowStmt = databaseConnection.prepareStatement(borrowingsQuery);
             PreparedStatement bookStmt = databaseConnection.prepareStatement(bookQuery)) {

            // Set borrowerID for borrowings query
            borrowStmt.setInt(1, borrowerID);

            ResultSet borrowingsResultSet = borrowStmt.executeQuery();

            // ObservableList to hold the data for tableView2
            ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();

            while (borrowingsResultSet.next()) {
                String isbn = borrowingsResultSet.getString("ISBN");
                Date borrowDate = borrowingsResultSet.getDate("borrowDate");
                Date returnDate = borrowingsResultSet.getDate("returnDate");

                // Fetch the book title using the ISBN
                bookStmt.setString(1, isbn);
                ResultSet bookResultSet = bookStmt.executeQuery();

                String bookTitle = "Unknown"; // Default title
                if (bookResultSet.next()) {
                    bookTitle = bookResultSet.getString("title");
                }

                // Create a Map to hold this row's data
                Map<String, Object> row = new HashMap<>();
                row.put("ISBN", isbn);
                row.put("Title", bookTitle);
                row.put("BorrowDate", borrowDate);
                row.put("ReturnDate", returnDate);

                // Add the row to the ObservableList
                data.add(row);
            }

            // Set the data to tableView2
            tableView2.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBorrowerContactDataFromDatabase() {
        if (databaseConnection == null) {
            System.err.println("Database connection is not initialized.");
            return;
        }

        borrowerContactMap.clear(); // Clear the map before populating it
        String query = "SELECT bName, bEmail FROM borrowerContact";

        try (PreparedStatement stmt = databaseConnection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String email = rs.getString("bEmail");
                String name = rs.getString("bName");

                borrowerContactMap.put(email, name); // Populate map

                // Debugging log
                System.out.println("Added to borrowerContactMap: Email = " + email + ", Name = " + name);
            }

            System.out.println("Total BorrowerContacts Loaded: " + borrowerContactMap.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void loadBorrowerDataFromDatabase() {
        if (databaseConnection == null) {
            System.err.println("Database connection is not initialized.");
            return;
        }

        borrowerList.clear();

        String query = "SELECT borrowerID, bEmail FROM borrower";

        try (PreparedStatement stmt = databaseConnection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Borrower borrower = new Borrower();
                borrower.setBorrowerID(rs.getInt("borrowerID"));
                borrower.setbEmail(rs.getString("bEmail"));

                borrowerList.add(borrower);

                // Log borrower data
                System.out.println("Added Borrower: ID = " + borrower.getBorrowerID() +
                        ", Email = " + borrower.getbEmail() +
                        ", Name = " + borrowerContactMap.get(borrower.getbEmail()));
            }

            System.out.println("Total Borrowers Loaded: " + borrowerList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void preloadBorrowingCounts() {
        if (databaseConnection == null) {
            System.err.println("Database connection is not initialized.");
            return;
        }

        borrowCountMap.clear();

        String query = "SELECT borrowerID, COUNT(*) AS borrowCount FROM borrowings GROUP BY borrowerID";

        try (PreparedStatement stmt = databaseConnection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int borrowerID = rs.getInt("borrowerID");
                int borrowCount = rs.getInt("borrowCount");
                borrowCountMap.put(borrowerID, borrowCount);

                // Log the borrowing count for debugging
                System.out.println("BorrowerID = " + borrowerID + ", BorrowCount = " + borrowCount);
            }

            System.out.println("Total Borrow Counts Preloaded: " + borrowCountMap.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



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
    public void setDatabaseConnection(Connection databaseConnection) {
        this.databaseConnection = databaseConnection;
        System.out.println("setDatabaseConnection() called. Database connection: " + databaseConnection);
        if (this.databaseConnection != null) {
            loadBorrowerDataFromDatabase();
            loadBorrowerContactDataFromDatabase();
            preloadBorrowingCounts();

        }
    }


}
