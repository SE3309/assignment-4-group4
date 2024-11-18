package se3309a.library;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LibraryController implements Initializable {
    @FXML
    private Menu aboutMenu;
    @FXML
    private MenuItem aboutusMenuItem;
    @FXML
    private MenuItem changePasswordMenuItem;
    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private Menu fileMenu;
    @FXML
    private MenuItem loginMenuItem;
    @FXML
    private MenuItem logoutMenuItem;
    @FXML
    private MenuBar mainMenu;
    @FXML
    private Menu userMenuItem;
    @FXML
    private Menu manageUserAccountsMenu;
    @FXML
    private MenuItem SearchAndHoldsMenuItem;
    @FXML
    private Menu SearchingMenu;
    @FXML
    private Menu accountInfoMenu;
    @FXML
    private MenuItem availableAndCheckedOutBooksMenuItem;
    @FXML
    private MenuItem bookBorrowingHistoryMenuItem;
    @FXML
    private MenuItem bookHistoryMenuItem;
    @FXML
    private MenuItem createReviewMenuItem;
    @FXML
    private MenuItem finesMenuItem;
    @FXML
    private MenuItem popularGenreMenuItem;
    @FXML
    private Menu reviewsMenu;
    @FXML
    private Menu staffMenu;
    @FXML
    private MenuItem userBookInfoMenuItem;
    private Connection conn;
    private DataStore account;
    private String userAccountName;
    public String DBPassword = "write password here";

    // Aya
    public void createUserAccount() throws Exception {
        // load the fxml file (the UI elements)
        FXMLLoader fxmlLoader = new FXMLLoader(LibraryController.class.getResource("createUserAccount-view.fxml"));
        // create the root node
        Parent newUser = fxmlLoader.load();
        CreateBorrowerAccountController createBorrowerAccountController = (CreateBorrowerAccountController) fxmlLoader.getController();
        createBorrowerAccountController.setLibraryController(this);
        createBorrowerAccountController.setDataStore(new BookTableAdapter(false), new BookAuthorTableAdapter(false),
                new BookGenreTableAdapter(false), new BookBorrowingsTableAdapter(false), new StaffTableAdapter(false),
                new StaffContactTableAdapter(false), new BorrowerTableAdapter(false), new BBorrowingsTableAdapter(false),
                new BorrowerContactTableAdapter(false), new GenreTableAdapter(false), new BorrowingsTableAdapter(false),
                new ReviewsTableAdapter(false), new HistoryLogTableAdapter(false), new FinesTableAdapter(false));
        // create new stage
        Stage stage = new Stage();
        stage.setScene(new Scene(newUser));
        // add icon to the About window
        stage.getIcons().add(new Image("file:src/main/resources/se3309a/library/books.png"));
        stage.setTitle("Create Borrower Account");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    // Aya
    public void login() throws Exception {
        // load the fxml file (the UI elements)
        FXMLLoader fxmlLoader = new FXMLLoader(LibraryController.class.getResource("login-View.fxml"));
        // create the root node
        Parent newUser = fxmlLoader.load();
        LoginController loginController = (LoginController) fxmlLoader.getController();
        loginController.setLibraryController(this);
        loginController.setDataStore(new BookTableAdapter(false), new BookAuthorTableAdapter(false),
                new BookGenreTableAdapter(false), new BookBorrowingsTableAdapter(false), new StaffTableAdapter(false),
                new StaffContactTableAdapter(false), new BorrowerTableAdapter(false), new BBorrowingsTableAdapter(false),
                new BorrowerContactTableAdapter(false), new GenreTableAdapter(false), new BorrowingsTableAdapter(false),
                new ReviewsTableAdapter(false), new HistoryLogTableAdapter(false), new FinesTableAdapter(false));
        // create new stage
        Stage stage = new Stage();
        stage.setScene(new Scene(newUser));
        // add icon
        stage.getIcons().add(new Image("file:src/main/resources/se3309a/library/books.png"));
        stage.setTitle("Login");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    // Melike
    public void calculateAvailableAndCheckedOutBooks() throws Exception{
        // load the fxml file (the UI elements)
        FXMLLoader fxmlLoader = new FXMLLoader(LibraryController.class.getResource("viewAvailableAndCheckedOutBooks-view.fxml"));
        // create the root node
        Parent newUser = fxmlLoader.load();
        ViewAvailableAndCheckedOutBooksController viewAvailableAndCheckedOutBooksController = (ViewAvailableAndCheckedOutBooksController) fxmlLoader.getController();
        viewAvailableAndCheckedOutBooksController.setLibraryController(this);
        viewAvailableAndCheckedOutBooksController.setDataStore(new BookTableAdapter(false), new BookAuthorTableAdapter(false),
                new BookGenreTableAdapter(false), new BookBorrowingsTableAdapter(false), new StaffTableAdapter(false),
                new StaffContactTableAdapter(false), new BorrowerTableAdapter(false), new BBorrowingsTableAdapter(false),
                new BorrowerContactTableAdapter(false), new GenreTableAdapter(false), new BorrowingsTableAdapter(false),
                new ReviewsTableAdapter(false), new HistoryLogTableAdapter(false), new FinesTableAdapter(false));
        // create new stage
        Stage stage = new Stage();
        stage.setScene(new Scene(newUser));
        // add icon to the About window
        stage.getIcons().add(new Image("file:src/main/resources/se3309a/library/books.png"));
        stage.setTitle("View Available and Checked-Out Books");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }


    // Sukhman
    public void checkFines() throws Exception{
        // load the fxml file (the UI elements)
        FXMLLoader fxmlLoader = new FXMLLoader(LibraryController.class.getResource("checkFines-view.fxml"));
        // create the root node
        Parent newUser = fxmlLoader.load();
        CheckFinesController checkFinesController = (CheckFinesController) fxmlLoader.getController();
        checkFinesController.setLibraryController(this);
        checkFinesController.setDataStore(new BookTableAdapter(false), new BookAuthorTableAdapter(false),
                new BookGenreTableAdapter(false), new BookBorrowingsTableAdapter(false), new StaffTableAdapter(false),
                new StaffContactTableAdapter(false), new BorrowerTableAdapter(false), new BBorrowingsTableAdapter(false),
                new BorrowerContactTableAdapter(false), new GenreTableAdapter(false), new BorrowingsTableAdapter(false),
                new ReviewsTableAdapter(false), new HistoryLogTableAdapter(false), new FinesTableAdapter(false));
        // create new stage
        Stage stage = new Stage();
        stage.setScene(new Scene(newUser));
        // add icon to the About window
        stage.getIcons().add(new Image("file:src/main/resources/se3309a/library/books.png"));
        stage.setTitle("Check Fines");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    // Aya
    public void createReview() throws Exception{
        // load the fxml file (the UI elements)
        FXMLLoader fxmlLoader = new FXMLLoader(LibraryController.class.getResource("createReview-view.fxml"));
        // create the root node
        Parent newUser = fxmlLoader.load();
        CreateReviewController createReviewController = (CreateReviewController) fxmlLoader.getController();
        createReviewController.setLibraryController(this);
        createReviewController.setDataStore(new BookTableAdapter(false), new BookAuthorTableAdapter(false),
                new BookGenreTableAdapter(false), new BookBorrowingsTableAdapter(false), new StaffTableAdapter(false),
                new StaffContactTableAdapter(false), new BorrowerTableAdapter(false), new BBorrowingsTableAdapter(false),
                new BorrowerContactTableAdapter(false), new GenreTableAdapter(false), new BorrowingsTableAdapter(false),
                new ReviewsTableAdapter(false), new HistoryLogTableAdapter(false), new FinesTableAdapter(false));
        // create new stage
        Stage stage = new Stage();
        stage.setScene(new Scene(newUser));
        // add icon to the About window
        stage.getIcons().add(new Image("file:src/main/resources/se3309a/library/books.png"));
        stage.setTitle("Create Review");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    // Shahed
    public void findBookBorrowingHistory() throws Exception{
        // load the fxml file (the UI elements)
        FXMLLoader fxmlLoader = new FXMLLoader(LibraryController.class.getResource("findBookBorrowingHistory-view.fxml"));
        // create the root node
        Parent newUser = fxmlLoader.load();
        FindBookBorrowingHistoryController findBookBorrowingHistoryController = (FindBookBorrowingHistoryController) fxmlLoader.getController();
        findBookBorrowingHistoryController.setLibraryController(this);
        findBookBorrowingHistoryController.setDataStore(new BookTableAdapter(false), new BookAuthorTableAdapter(false),
                new BookGenreTableAdapter(false), new BookBorrowingsTableAdapter(false), new StaffTableAdapter(false),
                new StaffContactTableAdapter(false), new BorrowerTableAdapter(false), new BBorrowingsTableAdapter(false),
                new BorrowerContactTableAdapter(false), new GenreTableAdapter(false), new BorrowingsTableAdapter(false),
                new ReviewsTableAdapter(false), new HistoryLogTableAdapter(false), new FinesTableAdapter(false));
        // create new stage
        Stage stage = new Stage();
        stage.setScene(new Scene(newUser));
        // add icon to the About window
        stage.getIcons().add(new Image("file:src/main/resources/se3309a/library/books.png"));
        stage.setTitle("Find Book Borrowing History");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    // Melike
    public void findMostPopularGenre() throws Exception{
        // load the fxml file (the UI elements)
        FXMLLoader fxmlLoader = new FXMLLoader(LibraryController.class.getResource("findMostPopularGenre-view.fxml"));
        // create the root node
        Parent newUser = fxmlLoader.load();
        FindMostPopularGenreController findMostPopularGenreController = (FindMostPopularGenreController) fxmlLoader.getController();
        findMostPopularGenreController.setLibraryController(this);
        findMostPopularGenreController.setDataStore(new BookTableAdapter(false), new BookAuthorTableAdapter(false),
                new BookGenreTableAdapter(false), new BookBorrowingsTableAdapter(false), new StaffTableAdapter(false),
                new StaffContactTableAdapter(false), new BorrowerTableAdapter(false), new BBorrowingsTableAdapter(false),
                new BorrowerContactTableAdapter(false), new GenreTableAdapter(false), new BorrowingsTableAdapter(false),
                new ReviewsTableAdapter(false), new HistoryLogTableAdapter(false), new FinesTableAdapter(false));
        // create new stage
        Stage stage = new Stage();
        stage.setScene(new Scene(newUser));
        // add icon to the About window
        stage.getIcons().add(new Image("file:src/main/resources/se3309a/library/books.png"));
        stage.setTitle("Find Most Popular Genre");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    // Rzan
    public void searchBooks() throws Exception{
        // load the fxml file (the UI elements)
        FXMLLoader fxmlLoader = new FXMLLoader(LibraryController.class.getResource("searchBooks-view.fxml"));
        // create the root node
        Parent newUser = fxmlLoader.load();
        SearchBooksController searchBooksController = (SearchBooksController) fxmlLoader.getController();
        searchBooksController.setLibraryController(this);
        searchBooksController.setDataStore(new BookTableAdapter(false), new BookAuthorTableAdapter(false),
                new BookGenreTableAdapter(false), new BookBorrowingsTableAdapter(false), new StaffTableAdapter(false),
                new StaffContactTableAdapter(false), new BorrowerTableAdapter(false), new BBorrowingsTableAdapter(false),
                new BorrowerContactTableAdapter(false), new GenreTableAdapter(false), new BorrowingsTableAdapter(false),
                new ReviewsTableAdapter(false), new HistoryLogTableAdapter(false), new FinesTableAdapter(false));
        // create new stage
        Stage stage = new Stage();
        stage.setScene(new Scene(newUser));
        // add icon to the About window
        stage.getIcons().add(new Image("file:src/main/resources/se3309a/library/books.png"));
        stage.setTitle("Search Books");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    // Sukhman
    public void viewBookHistory() throws Exception{
        // load the fxml file (the UI elements)
        FXMLLoader fxmlLoader = new FXMLLoader(LibraryController.class.getResource("viewBookHistory-view.fxml"));
        // create the root node
        Parent newUser = fxmlLoader.load();
        ViewBookHistoryController viewBookHistoryController = (ViewBookHistoryController) fxmlLoader.getController();
        viewBookHistoryController.setLibraryController(this);
        viewBookHistoryController.setDataStore(new BookTableAdapter(false), new BookAuthorTableAdapter(false),
                new BookGenreTableAdapter(false), new BookBorrowingsTableAdapter(false), new StaffTableAdapter(false),
                new StaffContactTableAdapter(false), new BorrowerTableAdapter(false), new BBorrowingsTableAdapter(false),
                new BorrowerContactTableAdapter(false), new GenreTableAdapter(false), new BorrowingsTableAdapter(false),
                new ReviewsTableAdapter(false), new HistoryLogTableAdapter(false), new FinesTableAdapter(false));
        // create new stage
        Stage stage = new Stage();
        stage.setScene(new Scene(newUser));
        // add icon to the About window
        stage.getIcons().add(new Image("file:src/main/resources/se3309a/library/books.png"));
        stage.setTitle("View Book History");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    // Shahed
    public void viewUserBookInfo() throws Exception{
        // load the fxml file (the UI elements)
        FXMLLoader fxmlLoader = new FXMLLoader(LibraryController.class.getResource("viewUserBookInfo-view.fxml"));
        // create the root node
        Parent newUser = fxmlLoader.load();
        ViewUserBookInfoController viewUserBookInfoController = (ViewUserBookInfoController) fxmlLoader.getController();
        viewUserBookInfoController.setLibraryController(this);
        viewUserBookInfoController.setDataStore(new BookTableAdapter(false), new BookAuthorTableAdapter(false),
                new BookGenreTableAdapter(false), new BookBorrowingsTableAdapter(false), new StaffTableAdapter(false),
                new StaffContactTableAdapter(false), new BorrowerTableAdapter(false), new BBorrowingsTableAdapter(false),
                new BorrowerContactTableAdapter(false), new GenreTableAdapter(false), new BorrowingsTableAdapter(false),
                new ReviewsTableAdapter(false), new HistoryLogTableAdapter(false), new FinesTableAdapter(false));
        // create new stage
        Stage stage = new Stage();
        stage.setScene(new Scene(newUser));
        // add icon to the About window
        stage.getIcons().add(new Image("file:src/main/resources/se3309a/library/books.png"));
        stage.setTitle("View User Book Info");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void showAbout() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("about-view.fxml"));
        Parent About = (Parent) fxmlLoader.load();
        Scene scene = new Scene(About);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image("file:src/main/resources/se3309a/library/books.png"));
        stage.setTitle("About Us");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void exit() {
        Stage stage = (Stage) mainMenu.getScene().getWindow();
        stage.close();
    }

    public void changePassword() throws Exception {
//        account = new UserAccountTableAdapter(false);
//        // load the fxml file (the UI elements)
//        FXMLLoader fxmlLoader = new FXMLLoader(LibraryController.class.getResource("ChangePassword-view.fxml"));
//
//        // create the root node
//        Parent changePassword = fxmlLoader.load();
//        ChangePasswordController changePasswordController = (ChangePasswordController) fxmlLoader.getController();
//        changePasswordController.setIPayrollController(this);
//        changePasswordController.setDataStore(account);
//
//        // create new stage
//        Stage stage = new Stage();
//        stage.setScene(new Scene(changePassword));
//        // add icon to the About window
//        stage.getIcons().add(new Image("file:src/main/resources/se3309a/library/books.png"));
//        stage.setTitle("Change Password");
//        stage.initModality(Modality.APPLICATION_MODAL);
//        stage.show();
    }



    public void deleteUserAccount() throws Exception {
        // load the fxml file (the UI elements)
        FXMLLoader fxmlLoader = new FXMLLoader(LibraryController.class.getResource("deleteUserAccount-view.fxml"));
        // create the root node
        Parent aUser = fxmlLoader.load();
        DeleteBorrowerAccountController deleteBorrowerAccountController = (DeleteBorrowerAccountController) fxmlLoader.getController();
        deleteBorrowerAccountController.setLibraryController(this);
        deleteBorrowerAccountController.setDataStore(new BookTableAdapter(false), new BookAuthorTableAdapter(false),
                new BookGenreTableAdapter(false), new BookBorrowingsTableAdapter(false), new StaffTableAdapter(false),
                new StaffContactTableAdapter(false), new BorrowerTableAdapter(false), new BBorrowingsTableAdapter(false),
                new BorrowerContactTableAdapter(false), new GenreTableAdapter(false), new BorrowingsTableAdapter(false),
                new ReviewsTableAdapter(false), new HistoryLogTableAdapter(false), new FinesTableAdapter(false));
        // create new stage
        Stage stage = new Stage();
        stage.setScene(new Scene(aUser));
        // add icon to the About window
        stage.getIcons().add(new Image("file:src/main/resources/se3309a/library/books.png"));
        stage.setTitle("Delete Borrower Account");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public void logout() {
        disableMenuItems();
    }

    public void enableAdminControls() {
        // Off
        SearchingMenu.setDisable(true);
        reviewsMenu.setDisable(true);
        accountInfoMenu.setDisable(true);
        loginMenuItem.setDisable(true);

        // On
        fileMenu.setDisable(false);
        logoutMenuItem.setDisable(false);
        mainMenu.setDisable(false);
        closeMenuItem.setDisable(false);
        staffMenu.setDisable(false);
        manageUserAccountsMenu.setDisable(false);
        userMenuItem.setVisible(true);
    }

    public void enableEmployeeControls() {
        // Off
        staffMenu.setDisable(true);
        loginMenuItem.setDisable(true);
        manageUserAccountsMenu.setDisable(true);

        // On
        fileMenu.setDisable(false);
        logoutMenuItem.setDisable(false);
        mainMenu.setDisable(false);
        closeMenuItem.setDisable(false);
        SearchingMenu.setDisable(true);
        reviewsMenu.setDisable(true);
        accountInfoMenu.setDisable(true);
        userMenuItem.setVisible(true);

    }

    public void disableMenuItems() {
        // Off
        SearchingMenu.setDisable(true);
        reviewsMenu.setDisable(true);
        accountInfoMenu.setDisable(true);
        staffMenu.setDisable(true);
        logoutMenuItem.setDisable(true);
        userMenuItem.setVisible(false);
        manageUserAccountsMenu.setDisable(true);

        // On
        fileMenu.setDisable(false);
        mainMenu.setDisable(false);
        closeMenuItem.setDisable(false);
        loginMenuItem.setDisable(false);
    }

    // set the logged-in username into the menu item
    public void setUserFullname(String name) {
        userMenuItem.setText(name);
    }

    // save user account name
    public void setUserName(String userAccountName) {
        this.userAccountName = userAccountName;
    }

    public String getUserFullname() {
        return userMenuItem.getText();
    }

    public String getUserAccountName() {
        return userAccountName;
    }

    public void displayAlert(String msg) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LibraryApplication.class.getResource("alert-View.fxml"));
            // create the root node
            Parent alertWindow = fxmlLoader.load();
            AlertController alertController = (AlertController) fxmlLoader.getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(alertWindow));
            stage.getIcons().add(new Image("file:src/main/resources/se3309a/library/books.png"));
            alertController.setAlertText(msg);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException ex1) {
            System.out.println("Error in Display Alert " + ex1);
        }
    }

    // Just hash the password to encrypt it. It is more secure if we add random salt before hashing.
    public String encrypt(String password) {
        MessageDigest crypto = null;
        try {
            crypto = MessageDigest.getInstance("SHA-256");
            byte[] passBytes = password.getBytes();
            byte[] passHash = crypto.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < passHash.length; i++) {
                sb.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));
            }
            String generatedPassword = sb.toString();
            return generatedPassword;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDBPassword(){
        return DBPassword;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ImageView face = new ImageView(new Image("file:src/main/resources/se3309a/library/UserIcon.png", 20, 20, true, true));
        userMenuItem.setGraphic(face);
        disableMenuItems();

        try {
            // Create a named constant for the URL
            // NOTE: This value is specific for Java DB
            // Create a connection to the database
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library",
                    "root",
                    DBPassword);
            // Create the administrator data if it is not already in the database
            new StaffTableAdapter(false);

        } catch (SQLException ex) {
            displayAlert(ex.getMessage());
        }
    }



}
