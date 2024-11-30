package se3309a.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DeleteBorrowerAccountController implements Initializable {
    @FXML
    private BorderPane mPane;

    @FXML
    private TextField name;
    @FXML
    private Button cancelBtn;
    @FXML
    private TextField email;
    @FXML
    private Button deleteBtn;
    @FXML
    private ComboBox ID;

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
    private final ObservableList<Integer> IDList = FXCollections.observableArrayList();
    private Borrower borrower;

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
        buildData();

    }

    protected BorderPane mainPane;
    protected Node newNode;
    protected Node hostingNode;

    public void init(BorderPane mainPane, Node newNode, Node hostingNode)
    {
        this.mainPane = mainPane;
        this.newNode = newNode;
        this.hostingNode = hostingNode;
    }

    public void buildData() {
        try {
            IDList.addAll(borrowerTable.getKeys());
        } catch (SQLException ex) {
            libraryController.displayAlert("Borrowers List: " + ex.getMessage(),mPane, mPane.getCenter());
        }
    }

    public void deleteAccount() {
        try {
            // Then delete borrower info
            borrowerContactTable.deleteOneRecord(borrower.getbEmail());
            BBorrowings bBorrowings = (BBorrowings) bBorrowingsTable.findOneRecord(String.valueOf(borrower.getBorrowerID()));
            if (bBorrowings.getBorrower().getBorrowerID() != 0) {
                bBorrowingsTable.deleteOneRecord(String.valueOf(borrower.getBorrowerID()));
            }

            Borrowings borrowings = (Borrowings) borrowingsTable.findOneRecord(String.valueOf(borrower.getBorrowerID()));
            if (borrowings.getBorrower().getBorrowerID() != 0) {
                BookBorrowings bookBorrowings = (BookBorrowings) bookBorrowingsTable.findOneRecord(String.valueOf(borrowings.getBorrowingID()));
                if (bookBorrowings.getBorrowings().getBorrowingID() != 0) {
                    bookBorrowingsTable.deleteOneRecord(String.valueOf(borrowings.getBorrowingID()));
                }
                borrowingsTable.deleteOneRecord(String.valueOf(borrower.getBorrowerID()));
            }
            Fines fines = (Fines) finesTable.findOneRecord(String.valueOf(borrower.getBorrowerID()));
            if (fines.getBorrower().getBorrowerID() != 0) {
                finesTable.deleteOneRecord(String.valueOf(borrower.getBorrowerID()));
            }
            Reviews reviews = (Reviews) reviewsTable.findOneRecord(String.valueOf(borrower.getBorrowerID()));
            if (reviews.getBorrower().getBorrowerID() != 0) {
                reviewsTable.deleteOneRecord(String.valueOf(borrower.getBorrowerID()));
            }

            borrowerTable.deleteOneRecord(borrower.getbEmail());
            libraryController.displayAlert("Borrower Account has been Deleted!", mPane, mPane.getCenter());

            mainPane.getChildren().remove(newNode);
                    mainPane.setCenter(hostingNode);
//            // Get current stage reference
//            Stage stage = (Stage) cancelBtn.getScene().getWindow();
//            // Close stage
//            stage.close();
        } catch (SQLException ex) {
            libraryController.displayAlert("Find Borrower Account: " + ex.getMessage(),mPane, mPane.getCenter());
        }
    }

    // retrieve the selected profile and update the screen
    public void getAccount() {
        try {
            borrower = (Borrower) borrowerTable.findOneRecord2(ID.getValue().toString());
            deleteBtn.setDisable(false);
            email.setText(String.valueOf(borrower.getbEmail()));
            BorrowerContact borrowerContact = (BorrowerContact) borrowerContactTable.findOneRecord(borrower.getbEmail());
            name.setText(borrowerContact.getName());

        } catch (SQLException ex) {
            libraryController.displayAlert("Find Profile: " + ex.getMessage(),mPane, mPane.getCenter());
        }
    }

    public void cancel() {
        mainPane.getChildren().remove(newNode);
                mainPane.setCenter(hostingNode);
//        // Get current stage reference
//        Stage stage = (Stage) cancelBtn.getScene().getWindow();
//        // Close stage
//        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ID.setItems(IDList);

    }
}
