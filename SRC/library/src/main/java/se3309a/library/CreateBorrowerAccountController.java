package se3309a.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
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


    public void cancel() {
        // Get current stage reference
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        // Close stage
        stage.close();
    }

    private void displayAlert(String msg) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("alert-View.fxml"));
            Parent ERROR = loader.load();
            AlertController controller = (AlertController) loader.getController();

            Scene scene = new Scene(ERROR);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.getIcons().add(new Image("file:src/main/resources/se3309a/library/books.png"));
            controller.setAlertText(msg);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException ex1) {

        }
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
//        id.setItems(data);
    }
}
