package se3309a.library;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class SearchBooksController implements Initializable {
    @FXML
    private BorderPane mPane;

    @FXML
    private TableColumn<Book, String> ISBNColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableView<Object> bookTableView;
    @FXML
    private TableColumn<Book, Date> dateColumn;
    @FXML
    private TableColumn<Book, String> genreColumn;
    @FXML
    private TableColumn<Book, String> genreDescriptionColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TextField ISBNFilter;

    @FXML
    private CheckBox ISBNBox;

    @FXML
    private CheckBox authorBox;

    @FXML
    private TextField authorFilter;

    @FXML
    private CheckBox dateBox;

    @FXML
    private TextField dateFilter;

    @FXML
    private Button exitBtn;

    @FXML
    private Button filterBtn;

    @FXML
    private Label filterText;

    @FXML
    private Label bookText;

    @FXML
    private CheckBox genreBox;

    @FXML
    private TextField genreFilter;

    @FXML
    private Button holdBtn;

    @FXML
    private Button reviewBtn;

    @FXML
    private Button sortBtn;
    @FXML
    private Button clearBtn;

    @FXML
    private ComboBox<String> sortCombo;

    @FXML
    private Label sortText;

    @FXML
    private CheckBox titleBox;

    @FXML
    private TextField titleFilter;
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
    private LoginController loginController = new LoginController();
    private CreateReviewController createReviewController = new CreateReviewController();
    final ObservableList<String> data = FXCollections.observableArrayList();
    private boolean sorted = false;
    private boolean filtered = false;
    private String sortedBy = "";
    private String filteredBy = "";
    private String filterTerm = "";
    private Borrower borrower;

    protected BorderPane mainPane;
    protected Node newNode;
    protected Node hostingNode;

    public void init(BorderPane mainPane, Node newNode, Node hostingNode)
    {
        this.mainPane = mainPane;
        this.newNode = newNode;
        this.hostingNode = hostingNode;
    }


    private Book book = new Book();

    // set origin controller
    public void setLibraryController(LibraryController controller) {
        libraryController = controller;
    }

    // get table references
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

    // close when exit button is clicked
    @FXML
    void close(ActionEvent event) {
        mainPane.getChildren().remove(newNode);
                mainPane.setCenter(hostingNode);
//        // Get current stage reference
//        Stage stage = (Stage) exitBtn.getScene().getWindow();
//        // Close stage
//        stage.close();
    }

    // enable author filter field and filter button when author checkbox is clicked
    @FXML
    void filterAuthor(ActionEvent event) {
        authorFilter.setDisable(!authorBox.isSelected());
        filterBtn.setDisable(!authorBox.isSelected());

        // disable all other filtering
        if (authorBox.isSelected()) {
            titleBox.setSelected(false);
            dateBox.setSelected(false);
            genreBox.setSelected(false);
            ISBNBox.setSelected(false);
            titleFilter.setDisable(true);
            genreFilter.setDisable(true);
            dateFilter.setDisable(true);
            ISBNFilter.setDisable(true);
        }
    }

    // enable date filter field when date checkbox is clicked
    @FXML
    void filterDate(ActionEvent event) {

        dateFilter.setDisable(!dateBox.isSelected());
        filterBtn.setDisable(!dateBox.isSelected());
        // disable all other filtering
        if (dateBox.isSelected()) {
            titleBox.setSelected(false);
            authorBox.setSelected(false);
            genreBox.setSelected(false);
            ISBNBox.setSelected(false);
            titleFilter.setDisable(true);
            authorFilter.setDisable(true);
            genreFilter.setDisable(true);
            ISBNFilter.setDisable(true);
        }
    }

    // enable genre filter field when genre checkbox is clicked
    @FXML
    void filterGenre(ActionEvent event) {

        genreFilter.setDisable(!genreBox.isSelected());
        filterBtn.setDisable(!genreBox.isSelected());
        // disable all other filtering
        if (genreBox.isSelected()) {
            titleBox.setSelected(false);
            authorBox.setSelected(false);
            dateBox.setSelected(false);
            ISBNBox.setSelected(false);
            titleFilter.setDisable(true);
            authorFilter.setDisable(true);
            dateFilter.setDisable(true);
            ISBNFilter.setDisable(true);

        }
    }

    // enable ISBN filter field when ISBN checkbox is clicked
    @FXML
    void filterISBN(ActionEvent event) {

        ISBNFilter.setDisable(!ISBNBox.isSelected());
        filterBtn.setDisable(!ISBNBox.isSelected());
        // disable all other filtering
        if (ISBNBox.isSelected()) {
            titleBox.setSelected(false);
            authorBox.setSelected(false);
            genreBox.setSelected(false);
            dateBox.setSelected(false);
            titleFilter.setDisable(true);
            authorFilter.setDisable(true);
            genreFilter.setDisable(true);
            dateFilter.setDisable(true);
        }
    }

    // enable title filter field when title checkbox is clicked
    @FXML
    void filterTitle(ActionEvent event) {
        titleFilter.setDisable(!titleBox.isSelected());
        filterBtn.setDisable(!titleBox.isSelected());
        // disable all other filtering
        if (titleBox.isSelected()) {
            dateBox.setSelected(false);
            authorBox.setSelected(false);
            genreBox.setSelected(false);
            ISBNBox.setSelected(false);
            ISBNFilter.setDisable(true);
            authorFilter.setDisable(true);
            genreFilter.setDisable(true);
            dateFilter.setDisable(true);

        }
    }

    // filter table when filter button is clicked
    @FXML
    void filter(ActionEvent event) throws SQLException {
        List<Object> list = new ArrayList<>();

        // if filter by title
        if (titleBox.isSelected()) {
            // if field is empty send warning
            if (titleFilter.getText() == null || titleFilter.getText().equals("")) {
                filterText.setText("Enter a filter term!");
            } else {
                clearBtn.setDisable(false);
                filtered = true;
                filteredBy = "title";
                filterTerm = titleFilter.getText();
                bookTableView.getItems().clear(); // empty table
                // get all books using filter and sorting selected
                list = bookTable.getAllRecords(sortedBy, filteredBy, filterTerm);
                // add books to table
                bookTableView.setItems(FXCollections.observableArrayList(list));
            }
        } else if (authorBox.isSelected()) {
            // if field is empty send warning
            if (authorFilter.getText() == null) {
                filterText.setText("Enter a filter term!");
            } else {
                clearBtn.setDisable(false);
                filtered = true;
                filteredBy = "author";
                filterTerm = authorFilter.getText();
                bookTableView.getItems().clear();
                // get all books using filter and sorting selected
                list = bookTable.getAllRecords(sortedBy, filteredBy, filterTerm);
                // add books to table
                bookTableView.setItems(FXCollections.observableArrayList(list));
            }
        } else if (dateBox.isSelected()) {
            // if field is empty send warning
            if (dateFilter.getText() == null) {
                filterText.setText("Enter a filter term!");
            } else {
                clearBtn.setDisable(false);
                filtered = true;
                filteredBy = "publishedDate";
                filterTerm = dateFilter.getText();
                bookTableView.getItems().clear();
                // get all books using filter and sorting selected
                list = bookTable.getAllRecords(sortedBy, filteredBy, filterTerm);
                // add books to table
                bookTableView.setItems(FXCollections.observableArrayList(list));
            }
        } else if (genreBox.isSelected()) {
            // if field is empty send warning
            if (genreFilter.getText() == null) {
                filterText.setText("Enter a filter term!");
            } else {
                clearBtn.setDisable(false);
                filtered = true;
                filteredBy = "genreType";
                filterTerm = genreFilter.getText();
                bookTableView.getItems().clear();
                // get all books using filter and sorting selected
                list = bookTable.getAllRecords(sortedBy, filteredBy, filterTerm);
                // add books to table
                bookTableView.setItems(FXCollections.observableArrayList(list));
            }
        } else {
            // if field is empty send warning
            if (ISBNFilter.getText() == null) {
                filterText.setText("Enter a filter term!");
            } else {
                clearBtn.setDisable(false);
                filtered = true;
                filteredBy = "ISBN";
                filterTerm = ISBNFilter.getText();
                bookTableView.getItems().clear();
                // get all books using filter and sorting selected
                list = bookTable.getAllRecords(sortedBy, filteredBy, filterTerm);
                // add books to table
                bookTableView.setItems(FXCollections.observableArrayList(list));
            }
        }
    }

    // puts table back to before filtering
    @FXML
    void clearFilter(ActionEvent event) throws SQLException {
        // reset all fields
        bookTableView.getItems().clear();
        List<Object> list = new ArrayList<>();
        titleFilter.setText("");
        authorFilter.setText("");
        ISBNFilter.setText("");
        dateFilter.setText("");
        genreFilter.setText("");
        dateBox.setSelected(false);
        authorBox.setSelected(false);
        genreBox.setSelected(false);
        ISBNBox.setSelected(false);
        titleBox.setSelected(false);
        filterBtn.setDisable(true);
        titleFilter.setDisable(true);
        genreFilter.setDisable(true);
        authorFilter.setDisable(true);
        dateFilter.setDisable(true);
        ISBNFilter.setDisable(true);
        // keeps track of original sorting before filtering
        if (sortedBy.equals("ISBN")) {
            // get all books with sorting
            list = bookTable.getAllRecords("ISBN");
            bookTableView.setItems(FXCollections.observableArrayList(list));
        } else if (sortedBy.equals("title")) {
            // get all books with sorting
            list = bookTable.getAllRecords("title");
            bookTableView.setItems(FXCollections.observableArrayList(list));
        } else if (sortedBy.equals("author")) {
            // get all books with sorting
            list = bookTable.getAllRecords("author");
            bookTableView.setItems(FXCollections.observableArrayList(list));
        } else if (sortedBy.equals("genreType")) {
            // get all books with sorting
            list = bookTable.getAllRecords("genreType");
            bookTableView.setItems(FXCollections.observableArrayList(list));
        } else if (sortedBy.equals("genreDescription")) {
            // get all books with sorting
            list = bookTable.getAllRecords("genreDescription");
            bookTableView.setItems(FXCollections.observableArrayList(list));
        } else if (sortedBy.equals("publishedDate")) {
            // get all books with sorting
            list = bookTable.getAllRecords("publishedDate");
            bookTableView.setItems(FXCollections.observableArrayList(list));
        } else {
            // get all books without sorting
            bookTableView.setItems(getBooks());
        }
        clearBtn.setDisable(true);
    }

    // adds hold when place hold button is clicked
    @FXML
    void hold(ActionEvent event) throws SQLException {
        // get selected book
         book = (Book) bookTableView.getSelectionModel().getSelectedItem();
        // if none selected sends warning
        if (bookTableView.getSelectionModel().getSelectedItem() == null) {
            libraryController.displayAlert("Please select a book first.",mPane, mPane.getCenter());
        } else {
            // finds borrower id and isbn in existing borrowings
            Borrower borrower1 = ((Borrowings) (borrowingsTable.findOneRecord(String.valueOf(borrower.getBorrowerID())))).getBorrower();
            Borrowings borrowings1 = ((Borrowings) (borrowingsTable.findOneRecord2(String.valueOf(book.getISBN()))));
            // if they don't exist in any borrowings, allow current borrowing to happen
            if (borrower1.getBorrowerID() == 0 && borrowings1.getBorrower().getBorrowerID() == 0) {
                Borrowings borrowings = new Borrowings();
                // get borrowing ids
                List<Integer> keys = borrowingsTable.getKeys();

                // if none, set this one to 1
                if (keys.isEmpty()) {
                    borrowings.setBorrowingID(1);
                    borrowings.setBorrowDate(new java.sql.Date(System.currentTimeMillis()));
                    borrowings.setStatus("On Hold");
                    borrowings.setBook(book);
                    borrowings.setBorrower(borrower);
                    // add borrowing
                    borrowingsTable.addNewRecord(borrowings);
                } else {
                    // get the last borrowing id
                    int max = keys.getLast();
                    max++; // increase it for this next one
                    borrowings.setBorrowingID(max);
                    borrowings.setBorrowDate(new java.sql.Date(System.currentTimeMillis()));
                    borrowings.setStatus("On Hold");
                    borrowings.setBook(book);
                    borrowings.setBorrower(borrower);
                    // add borrowing
                    borrowingsTable.addNewRecord(borrowings);
                }

                // fill other related tables
                BBorrowings bBorrowings = new BBorrowings();
                bBorrowings.setBorrower(borrower);
                bBorrowings.setBorrowings(borrowings);

                BookBorrowings bookBorrowings = new BookBorrowings();
                bookBorrowings.setBook(book);
                bookBorrowings.setBorrowings(borrowings);

                bBorrowingsTable.addNewRecord(bBorrowings);
                bookBorrowingsTable.addNewRecord(bookBorrowings);

                libraryController.displayAlert("Hold has been placed on " + book.getTitle() + " for 20 days!", mPane, mPane.getCenter());
            } else {
                // if account already has a hold
                if (borrower1.getBorrowerID() != 0) {
                    libraryController.displayAlert("This account already has holds out!",mPane, mPane.getCenter());
                } else { // if book has already been taken out by someone else
                    libraryController.displayAlert("This book is already on a hold!",mPane, mPane.getCenter());
                }
            }

        }

    }

    // go to reviews window when review button is clicked
    @FXML
    void review(ActionEvent event) throws Exception {
        // sends warning if no book is selected
        if (bookTableView.getSelectionModel().getSelectedItem() == null) {
            libraryController.displayAlert("Please select a book first.", mPane, mPane.getCenter());
        } else {
            book = (Book) bookTableView.getSelectionModel().getSelectedItem();
            libraryController.setISBN(book.getISBN());
            libraryController.createReview();
        }
    }

    // sort table when sort button is clicked
    @FXML
    void sort(ActionEvent event) throws SQLException {
        // sends warning if no sorting is selected
        if (sortCombo.getValue() == null) {
            sortText.setText("Select a sorting option!");
        } else {
            sorted = true;
            // clear current table
            bookTableView.getItems().clear();
            List<Object> list = new ArrayList<>();
            String sort = sortCombo.getValue();
            // saves filtering if applied
            if (filtered) {
                // adds books according to selected sorting and filter
                if (sort.equals("ISBN")) {
                    list = bookTable.getAllRecords("ISBN", filteredBy, filterTerm);
                    sortedBy = "ISBN";
                } else if (sort.equals("Title")) {// adds books according to selected sorting and filter
                    list = bookTable.getAllRecords("title", filteredBy, filterTerm);
                    sortedBy = "title";
                } else if (sort.equals("Author")) {// adds books according to selected sorting and filter
                    list = bookTable.getAllRecords("author", filteredBy, filterTerm);
                    sortedBy = "author";
                } else if (sort.equals("Genre")) {// adds books according to selected sorting and filter
                    list = bookTable.getAllRecords("genreType", filteredBy, filterTerm);
                    sortedBy = "genreType";
                } else {// adds books according to selected sorting and filter
                    list = bookTable.getAllRecords("publishedDate", filteredBy, filterTerm);
                    sortedBy = "publishedDate";
                }
            } else { // if no filter is applied
                if (sort.equals("ISBN")) {
                    list = bookTable.getAllRecords("ISBN");
                    sortedBy = "ISBN";
                } else if (sort.equals("Title")) {
                    list = bookTable.getAllRecords("title");
                    sortedBy = "title";
                } else if (sort.equals("Author")) {
                    list = bookTable.getAllRecords("author");
                    sortedBy = "author";
                } else if (sort.equals("Genre")) {
                    list = bookTable.getAllRecords("genreType");
                    sortedBy = "genreType";
                } else {
                    list = bookTable.getAllRecords("publishedDate");
                    sortedBy = "publishedDate";
                }
            }
            bookTableView.setItems(FXCollections.observableArrayList(list));
        }

    }

    // get all books
    public ObservableList<Object> getBooks() {
        try {
            List<Object> list = bookTable.getAllRecords();
            return FXCollections.observableArrayList(list);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    // get current user's borrower id
    public void buildData(int borrowerID) {
        borrower.setBorrowerID(borrowerID);
    }

    // initialize values
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // take out warning texts
        sortText.setText("");
        filterText.setText("");
        bookText.setText((""));

        borrower = new Borrower();

        // populate sorting combo box
        String[] sorting =
                {"ISBN", "Title", "Author",
                        "Genre", "Published Date"};
        sortCombo.setItems(FXCollections
                .observableArrayList(sorting));
        try {
            setDataStore(new BookTableAdapter(false), new BookAuthorTableAdapter(false),
                    new BookGenreTableAdapter(false), new BookBorrowingsTableAdapter(false), new StaffTableAdapter(false),
                    new StaffContactTableAdapter(false), new BorrowerTableAdapter(false), new BBorrowingsTableAdapter(false),
                    new BorrowerContactTableAdapter(false), new GenreTableAdapter(false), new BorrowingsTableAdapter(false),
                    new ReviewsTableAdapter(false), new HistoryLogTableAdapter(false), new FinesTableAdapter(false));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // populate book tableview
        ISBNColumn.setCellValueFactory(
                cellData -> cellData.getValue().ISBNProperty());
        titleColumn.setCellValueFactory(
                cellData -> cellData.getValue().titleProperty());
        authorColumn.setCellValueFactory(
                cellData -> cellData.getValue().authorProperty());
        genreColumn.setCellValueFactory(
                cellData -> cellData.getValue().genreTypeProperty());
        genreDescriptionColumn.setCellValueFactory(
                cellData -> cellData.getValue().genreDescriptionProperty());
        dateColumn.setCellValueFactory(
                cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getPublishedDate()));

        bookTableView.setItems(getBooks());

    }
}
