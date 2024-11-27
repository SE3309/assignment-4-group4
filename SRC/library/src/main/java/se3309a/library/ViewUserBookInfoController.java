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

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewUserBookInfoController {
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
    private TableColumn<Borrower, Number> idColumn, numOfBooksBorrowedColumn;
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


    private DataStore bookTable, bookAuthorTable, bookGenreTable, bookBorrowingsTable, staffTable, staffContactTable, borrowerTable, bBorrowingsTable, borrowerContactTable, genreTable, borrowingsTable, reviewsTable, historyLogTable, finesTable;
    // List to hold Borrower data
    private ObservableList<Borrower> borrowerList = FXCollections.observableArrayList();

    private LibraryController libraryController;
    final ObservableList<String> data = FXCollections.observableArrayList();
    private Map<String, String> borrowerContactMap = new HashMap<>();
    private Map<Integer, Integer> borrowCountMap = new HashMap<>();

    private ObservableList<BorrowerContact> borrowerContactList = FXCollections.observableArrayList();
    private Connection databaseConnection;


    public void initialize() throws SQLException {
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
            return new SimpleStringProperty(name != null ? name : "Unknown Name");
        });
        numOfBooksBorrowedColumn.setCellValueFactory(cellData -> {
            int borrowerID = cellData.getValue().getBorrowerID(); // Get Borrower ID
            int borrowCount = borrowCountMap.getOrDefault(borrowerID, 0); // Fetch count from map
            return new SimpleIntegerProperty(borrowCount); // Return as a property for binding
        });


        // Bind borrower list to the TableView
        tableView.setItems(borrowerList);

        try {
            setDataStore(new BookTableAdapter(false), new BookAuthorTableAdapter(false),
                    new BookGenreTableAdapter(false), new BookBorrowingsTableAdapter(false), new StaffTableAdapter(false),
                    new StaffContactTableAdapter(false), new BorrowerTableAdapter(false), new BBorrowingsTableAdapter(false),
                    new BorrowerContactTableAdapter(false), new GenreTableAdapter(false), new BorrowingsTableAdapter(false),
                    new ReviewsTableAdapter(false), new HistoryLogTableAdapter(false), new FinesTableAdapter(false));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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

    }

    public void viewUserBookHistory(ActionEvent event) throws SQLException {
        // Get selected borrower from tableView
        Borrower selectedBorrower = tableView.getSelectionModel().getSelectedItem();

        if (selectedBorrower == null) {
            libraryController.displayAlert("No borrower selected.");
            return;
        }

        int borrowerID = selectedBorrower.getBorrowerID();

        // Clear existing data in tableView2
        tableView2.getItems().clear();

        Borrowings borrowings = (Borrowings) bookBorrowingsTable.findOneRecord(String.valueOf(borrowerID), "");

        // ObservableList to hold the data for tableView2
        ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();

        // Create a Map to hold this row's data
        Map<String, Object> row = new HashMap<>();
        row.put("ISBN", borrowings.getBook().getISBN());
        row.put("Title", borrowings.getBook().getTitle());
        row.put("BorrowDate", borrowings.getBorrowDate());
        row.put("ReturnDate", borrowings.getReturnDate());

        // Add the row to the ObservableList
        data.add(row);

        // Set the data to tableView2
        tableView2.setItems(data);
    }

    private void loadBorrowerContactDataFromDatabase() throws SQLException {
        if (databaseConnection == null) {
            System.err.println("Database connection is not initialized.");
            return;
        }

        borrowerContactMap.clear(); // Clear the map before populating it
        List<Object> list = borrowerContactTable.getAllRecords();
        for (int i = 0; i < list.size(); i++) {
            String email = ((BorrowerContact) list.get(i)).getBorrower().getbEmail();
            String name = ((BorrowerContact) list.get(i)).getName();
            borrowerContactMap.put(email, name);
        }

    }


    private void loadBorrowerDataFromDatabase() throws SQLException {
        if (databaseConnection == null) {
            System.err.println("Database connection is not initialized.");
            return;
        }

        borrowerList.clear();

        List<Object> list = borrowerTable.getAllRecords();
        for (int i = 0; i < list.size(); i++) {
            Borrower borrower = (Borrower) list.get(i);
            borrowerList.add(borrower);
        }

    }

    private void preloadBorrowingCounts() throws SQLException {
        if (databaseConnection == null) {
            System.err.println("Database connection is not initialized.");
            return;
        }

        borrowCountMap.clear();
        List<Object> list = bBorrowingsTable.getAllRecords();
        for (int i = 0; i < list.size(); i++) {
            int borrowerID = ((Borrowings) list.get(i)).getBorrower().getBorrowerID();
            int borrowCount = ((Borrowings) list.get(i)).getBorrowCount();

            borrowCountMap.put(borrowerID, borrowCount);
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

    public void setDatabaseConnection(Connection databaseConnection) throws SQLException {
        try {
            setDataStore(new BookTableAdapter(false), new BookAuthorTableAdapter(false),
                    new BookGenreTableAdapter(false), new BookBorrowingsTableAdapter(false), new StaffTableAdapter(false),
                    new StaffContactTableAdapter(false), new BorrowerTableAdapter(false), new BBorrowingsTableAdapter(false),
                    new BorrowerContactTableAdapter(false), new GenreTableAdapter(false), new BorrowingsTableAdapter(false),
                    new ReviewsTableAdapter(false), new HistoryLogTableAdapter(false), new FinesTableAdapter(false));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.databaseConnection = databaseConnection;
        if (this.databaseConnection != null) {
            loadBorrowerDataFromDatabase();
            loadBorrowerContactDataFromDatabase();
            preloadBorrowingCounts();

        }
    }


}
