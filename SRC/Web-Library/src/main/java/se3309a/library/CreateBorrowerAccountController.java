package se3309a.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateBorrowerAccountController implements Initializable {
    @FXML
    private Button cancelBtn;

    @FXML
    private TextField email;

    @FXML
    private Label errorMsg;

    @FXML
    private TextField fullName;

    @FXML
    private PasswordField password1;

    @FXML
    private PasswordField password2;

    @FXML
    private Button saveBtn;


    @FXML
    private TextField userName;
    @FXML
    private ComboBox<String> id;
    private String loggedInUser;
    final ObservableList<String> data = FXCollections.observableArrayList();
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

    protected BorderPane mainPane;
    protected Node newNode;
    protected Node hostingNode;

    public void init(BorderPane mainPane, Node newNode, Node hostingNode) {
        this.mainPane = mainPane;
        this.newNode = newNode;
        this.hostingNode = hostingNode;
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

    @FXML
    private void saveBtnAction() {
        saveBorrowerAccount();

    }
    public void saveBorrowerAccount() {
        try {
            // Validate inputs
            String borrowerEmail = email.getText().trim();
            String borrowerPassword1 = password1.getText().trim();
            String borrowerPassword2 = password2.getText().trim();

            if (borrowerEmail.isEmpty() || borrowerPassword1.isEmpty() || borrowerPassword2.isEmpty()) {
                libraryController.displayAlert("All fields are required!", mainPane, mainPane.getCenter());
                return;
            }

            if (!borrowerPassword1.equals(borrowerPassword2)) {
                libraryController.displayAlert("Passwords do not match!", mainPane, mainPane.getCenter());
                return;
            }else if(borrowerPassword1.length() < 6){
                libraryController.displayAlert("Password is too short!!", mainPane, mainPane.getCenter());
                return;
            }

            // Check if the email already exists in the database
            if (borrowerTable.isRegistered(borrowerEmail)) {
                libraryController.displayAlert("Email has an existing account!", mainPane, mainPane.getCenter());
                return;
            }
            // Create a Borrower object
            Borrower borrower = new Borrower();
            BorrowerContact borrowerContact = new BorrowerContact();
            borrower.setbEmail(borrowerEmail);
            borrower.setbPassword(borrowerPassword1);
            borrower.setMembershipDate(new java.sql.Date(System.currentTimeMillis()));
            borrowerContact.setBorrower(borrower);
            borrowerContact.setName(userName.getText());
            // Insert borrower into the database
            borrowerTable.addNewRecord(borrower);
            borrowerContactTable.addNewRecord(borrowerContact);

            // Show success message
            libraryController.displayAlert("Borrower account created successfully!", mainPane, mainPane.getCenter());
            clearForm();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            libraryController.displayAlert("Error saving borrower: " + ex.getMessage(), mainPane, mainPane.getCenter());
        }
    }


    private void clearForm() {
        email.clear();
        userName.clear();
        password1.clear();
        password2.clear();
    }


    public void cancel() {
        mainPane.getChildren().remove(newNode);
        mainPane.setCenter(hostingNode);
//        // Get current stage reference
//        Stage stage = (Stage) cancelBtn.getScene().getWindow();
//        // Close stage
//        stage.close();
    }



    public void clearErrorMsg() {
        errorMsg.setText("");
    }

    public void buildData() {
//        try {
//            data.addAll(employeeTable.getKeys());
//        } catch (SQLException ex) {
//            displayAlert("ERROR - employeeAdapter: " + ex.getMessage());
//        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        saveBtn.setOnAction(event -> saveBtnAction());
    }

//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
////        id.setItems(data);
//    }


}
