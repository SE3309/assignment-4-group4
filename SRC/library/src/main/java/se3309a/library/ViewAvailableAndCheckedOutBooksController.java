package se3309a.library;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewAvailableAndCheckedOutBooksController implements Initializable {
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
    private TableColumn<Book, String> ISBN1Column;

    @FXML
    private TableColumn<Book, String> ISBN2Column;

    @FXML
    private TableView<Object> availableBooksTable;

    @FXML
    private TableView<Object> checkedOutBooksTable;

    @FXML
    private TableColumn<Book, String> title1Column;

    @FXML
    private TableColumn<Book, String> title2Column;


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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // populate tableview
        ISBN1Column.setCellValueFactory(
                cellData -> cellData.getValue().ISBNProperty());
        ISBN2Column.setCellValueFactory(
                cellData -> cellData.getValue().ISBNProperty());
        title1Column.setCellValueFactory(
                cellData -> cellData.getValue().titleProperty());
        title2Column.setCellValueFactory(
                cellData -> cellData.getValue().titleProperty());
        try {
            setDataStore(new BookTableAdapter(false), new BookAuthorTableAdapter(false),
                    new BookGenreTableAdapter(false), new BookBorrowingsTableAdapter(false), new StaffTableAdapter(false),
                    new StaffContactTableAdapter(false), new BorrowerTableAdapter(false), new BBorrowingsTableAdapter(false),
                    new BorrowerContactTableAdapter(false), new GenreTableAdapter(false), new BorrowingsTableAdapter(false),
                    new ReviewsTableAdapter(false), new HistoryLogTableAdapter(false), new FinesTableAdapter(false));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            List<Object> list = new ArrayList<>();
            list = borrowingsTable.getAllRecords("");
            availableBooksTable.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            List<Object> list = new ArrayList<>();
            list = borrowingsTable.getAllRecords("", "", "");
            checkedOutBooksTable.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}
