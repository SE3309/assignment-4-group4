package se3309a.library;

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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;


public class ViewUserBookInfoController  {
    @FXML
    private ComboBox<String> criteriaCombo;
    @FXML
    private TextField searchField;

    @FXML
    private Label emailOrNameLabel;
    @FXML
    private TableView<Borrower> tableView; // Update TableView to hold Borrower objects
    @FXML
    private TableColumn<Borrower, Number> idColumn; // Update column to hold Number type
    @FXML
    private TableColumn<Borrower, String> nameColumn, emailColumn;
    @FXML
    private TableColumn<Borrower, Number> numOfBooksBorrowedColumn;

    @FXML
    private Button searchButton,viewBorrowingHistoryButton;
    private DataStore bookTable,bookAuthorTable,bookGenreTable,bookBorrowingsTable,staffTable,staffContactTable,borrowerTable,bBorrowingsTable,borrowerContactTable,genreTable,borrowingsTable,reviewsTable,historyLogTable,finesTable;
    // List to hold Borrower data
    private ObservableList<Borrower> borrowerList = FXCollections.observableArrayList();

    private LibraryController libraryController;
    final ObservableList<String> data = FXCollections.observableArrayList();
    private Map<String, String> borrowerContactMap = new HashMap<>();

    private ObservableList<BorrowerContact> borrowerContactList = FXCollections.observableArrayList();
    private Connection databaseConnection;

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
        // Add your logic for viewing borrowing history
        System.out.println("View User Book History button clicked");
    }

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
        // Cross-reference Borrower emails with names from BorrowerContact for the name column
        // Cross-reference emails for the name column
        nameColumn.setCellValueFactory(cellData -> {
            String email = cellData.getValue().getbEmail();
            String name = borrowerContactMap.get(email); // Fetch name from map using email
            return new SimpleStringProperty(name != null ? name : "Unknown");
        });

        // Bind borrower list to the TableView
        tableView.setItems(borrowerList);

        // Load data
        loadBorrowerContactDataFromDatabase(); // Load BorrowerContact data first
        loadBorrowerDataFromDatabase();       // Load Borrower data
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

        }
    }
}
