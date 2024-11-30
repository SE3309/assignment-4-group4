package se3309a.library;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
public class viewReviewsController implements Initializable {
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
    private Borrower borrower;

    private Book book;

    @FXML
    private Button backBtn;
    @FXML
    private TextArea reviewTextArea;           // TextArea for entering the review text

    @FXML
    private TableColumn<Reviews, Integer> ratingColumn;

    @FXML
    private TableColumn<Reviews, Date> reviewDateColumn;

    @FXML
    private TableView<Object> viewReviewTable;

    @FXML
    private TableColumn<Reviews, String> reviewTextColumn;

    @FXML
    private TableColumn<Reviews, String> isbnColumn;

    protected BorderPane mainPane;
    protected Node node;

    public void init(BorderPane mainPane, Node node)
    {
        this.mainPane = mainPane;
        this.node = node;
    }



    @FXML
    void close(ActionEvent event) {
        mainPane.getChildren().remove(node);
//        // Get current stage reference
//        Stage stage = (Stage) backBtn.getScene().getWindow();
//        // Close stage
//        stage.close();
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


    public void setDataStore(DataStore reviews) {
        this.reviewsTable = reviews;
    }
    public void setLibraryController(LibraryController controller) {
        libraryController = controller;
    }
    public void buildData(int borrowerID, String ISBN) {
        borrower.setBorrowerID(borrowerID);
        book.setISBN(ISBN);
    }

    public ObservableList<Object> getReviews() {
        try {
            List<Object> list = reviewsTable.getAllRecords();
            return FXCollections.observableArrayList(list);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        borrower = new Borrower();
        book = new Book();
        try {
            setDataStore(new BookTableAdapter(false), new BookAuthorTableAdapter(false),
                    new BookGenreTableAdapter(false), new BookBorrowingsTableAdapter(false), new StaffTableAdapter(false),
                    new StaffContactTableAdapter(false), new BorrowerTableAdapter(false), new BBorrowingsTableAdapter(false),
                    new BorrowerContactTableAdapter(false), new GenreTableAdapter(false), new BorrowingsTableAdapter(false),
                    new ReviewsTableAdapter(false), new HistoryLogTableAdapter(false), new FinesTableAdapter(false));
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
        // populate book tableview
        ratingColumn.setCellValueFactory(
                cellData -> new SimpleObjectProperty(cellData.getValue().ratingProperty().get()));
        reviewTextColumn.setCellValueFactory(
                cellData -> cellData.getValue().reviewTextProperty());
        isbnColumn.setCellValueFactory(
                cellData -> cellData.getValue().getBook().ISBNProperty());
        reviewDateColumn.setCellValueFactory(
                cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getReviewDate()));

        viewReviewTable.setItems(getReviews());

    }

}


