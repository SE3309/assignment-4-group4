package se3309a.library;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class CreateReviewController implements Initializable {
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
    private ComboBox<Integer> ratingComboBox;  // ComboBox for rating 1-10
    @FXML
    private TextField reviewTextField;           // TextArea for entering the review text

    @FXML
    private TableColumn<Reviews, String> borrowerEmailColumn;
    @FXML
    private TableColumn<Reviews, String> ISBNColumn;
    @FXML
    private TableColumn<Reviews, Integer> ratingColumn;

    @FXML
    private Button refreshButton;

    @FXML
    private TableColumn<Reviews, Date> reviewDateColumn;

    @FXML
    private TableView<Object> reviewTableView;

    @FXML
    private TableColumn<Reviews, String> reviewTextColumn;

    @FXML
    private Button backButton;

    private Reviews reviews = new Reviews();


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
    @FXML
    void handleAddReview(ActionEvent event) throws SQLException {
        createReview();

    }

    public void setDataStore(DataStore reviews) {
        this.reviewsTable = reviews;
    }

    public void buildData(int borrowerID, String ISBN) {
        borrower.setBorrowerID(borrowerID);
        book.setISBN(ISBN);
    }
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
   public void close(ActionEvent event) {
        // Get current stage reference
        Stage stage = (Stage) backButton.getScene().getWindow();
        // Close stage
        stage.close();
    }

    public void createReview() throws SQLException {
        // Validate inputs
        Integer rating = ratingComboBox.getValue();
        String reviewText = reviewTextField.getText();
        if (rating == null) {
            showErrorMessage("Rating is required.");
            return;
        }

        if (borrower.getBorrowerID() == 0 || book.getISBN() == null) {
            showErrorMessage("Borrower and book must be set.");
            return;
        }
        // get review ids
        List<Integer> keys = reviewsTable.getKeys();
        Reviews newReview = new Reviews();

        // if none, set this one to 1
        if (keys.isEmpty()) {
            newReview.setReviewID(1);
            newReview.setBorrower(borrower);
            newReview.setBook(book);
            newReview.setReviewText(reviewText);
            newReview.setRating(rating);
            newReview.setReviewDate(new java.sql.Date(System.currentTimeMillis())); // Set current date
            // add review
            reviewsTable.addNewRecord(newReview);
        } else {
            // get the last review id
            int max = keys.getLast();
            max++; // increase it for this next one
            newReview.setReviewID(max);
            newReview.setBorrower(borrower);
            newReview.setBook(book);
            newReview.setReviewText(reviewText);
            newReview.setRating(rating);
            newReview.setReviewDate(new java.sql.Date(System.currentTimeMillis())); // Set current date
            // add review
            reviewsTable.addNewRecord(newReview);
        }
        reviewTableView.getItems().clear();
        // Refresh the table
        reviewTableView.setItems(getReviews());
        // Clear the input fields
        reviewTextField.clear();
        ratingComboBox.setValue(null);
        libraryController.displayAlert("Review added successfully.");
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

        Integer[] rates =
                {1, 2, 3,
                        4, 5, 6, 7, 8, 9, 10};
        ratingComboBox.setItems(FXCollections
                .observableArrayList(rates));
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
        ISBNColumn.setCellValueFactory(
                cellData -> cellData.getValue().getBook().ISBNProperty());
        reviewDateColumn.setCellValueFactory(
                cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getReviewDate()));

        reviewTableView.setItems(getReviews());


    }
}



