package se3309a.library;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ViewBookHistoryController implements Initializable {
    @FXML
    private BorderPane mPane;

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
    private TableView<Object> bookHistoryTable;

    @FXML
    private TableColumn<Borrowings, Date> returnDateColumn;

    @FXML
    private TableColumn<Borrowings, String> statusColumn;

    Borrower borrower;

    protected BorderPane mainPane;
    protected Node newNode;
    protected Node hostingNode;

    public void init(BorderPane mainPane, Node newNode, Node hostingNode)
    {
        this.mainPane = mainPane;
        this.newNode = newNode;
        this.hostingNode = hostingNode;
    }

    public void exit() {
        mainPane.getChildren().remove(newNode);
                mainPane.setCenter(hostingNode);
    }

    @FXML
    void printHistory(ActionEvent event) throws SQLException {
        bookHistoryTable.getItems().clear();
        // Retrieve the borrowing ID for the borrower
        Borrowings borrowing = (Borrowings) borrowingsTable.findOneRecord(String.valueOf(borrower.getBorrowerID()));
        if (borrowing == null || borrowing.getBorrowingID() <= 0) {
            libraryController.displayAlert("No book history!",mPane, mPane.getCenter());
        } else {
            List<Object> list = new ArrayList<>();
            list.add(borrowing);
            // Add the fine to the ObservableList
            bookHistoryTable.setItems(FXCollections.observableArrayList(list));
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

    public void buildData(int borrowerID) {
        borrower.setBorrowerID(borrowerID);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        borrower = new Borrower();
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
    }

}