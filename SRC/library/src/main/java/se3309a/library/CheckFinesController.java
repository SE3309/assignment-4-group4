package se3309a.library;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.List;
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
    private TableView<Object> fineTable;

    Borrower borrower;

    @FXML
    private Button printBtn;

    @FXML
    void print(ActionEvent event) throws SQLException {
        Fines fines = (Fines) finesTable.findOneRecord(String.valueOf(borrower.getBorrowerID()));
        List<Object> list = finesTable.getAllRecords(String.valueOf(borrower.getBorrowerID()));
        if (fines == null || fines.getFineID() <= 0) {
            libraryController.displayAlert("No fines!");
        } else {
            fineTable.setItems(FXCollections.observableArrayList(list));
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

    // get current user's borrower id
    public void buildData(int borrowerID) {
        borrower.setBorrowerID(borrowerID);
    }

    // get all fines
    public ObservableList<Object> getFines() {
        try {
            List<Object> list = finesTable.getAllRecords(String.valueOf(borrower.getBorrowerID()));
            return FXCollections.observableArrayList(list);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        borrower = new Borrower();
        try {
            setDataStore(new BookTableAdapter(false), new BookAuthorTableAdapter(false),
                    new BookGenreTableAdapter(false), new BookBorrowingsTableAdapter(false), new StaffTableAdapter(false),
                    new StaffContactTableAdapter(false), new BorrowerTableAdapter(false), new BBorrowingsTableAdapter(false),
                    new BorrowerContactTableAdapter(false), new GenreTableAdapter(false), new BorrowingsTableAdapter(false),
                    new ReviewsTableAdapter(false), new HistoryLogTableAdapter(false), new FinesTableAdapter(false));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    }
}
