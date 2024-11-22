package se3309a.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReviewsTableAdapter implements DataStore{
    private Connection connection;
    LibraryController libraryController = new LibraryController();
    //private String DB_URL = "jdbc:mysql://localhost:3306/library";
    public ReviewsTableAdapter(Boolean reset) throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());
        Statement stmt = connection.createStatement();
        if (reset) {
            try {
                // Remove tables if database tables have been created.
                // This will throw an exception if the tables do not exist
                stmt.execute("DROP TABLE reviews");
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
            }
        }
        try {
            String command = "CREATE TABLE reviews ("
                    + "reviewID INT NOT NULL AUTO_INCREMENT,"
                    + "reviewDate DATE NOT NULL,"
                    + "borrowerID INT NOT NULL,"
                    + "reviewText TEXT,"
                    + "rating INT NOT NULL,"
                    + "ISBN CHAR(13) NOT NULL,"
                    + "FOREIGN KEY (borrowerID) REFERENCES borrower(borrowerID),"
                    + "FOREIGN KEY (ISBN) REFERENCES book(ISBN),"
                    + "PRIMARY KEY (reviewID)"
                    + ")";
            // Create the table
            stmt.execute(command);
        } catch (SQLException ex) {
            // No need to report an error.
            // The table exists and may have some data.
            // We will use it instead of creating a new one.
        }
        connection.close();
    }

    // adds new record
    @Override
    public void addNewRecord(Object data) throws SQLException {
        Reviews reviews = (Reviews) data;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());

        Statement stmt = connection.createStatement();
        String command = "INSERT INTO reviews ( reviewID, reviewDate, borrowerID, reviewText, rating, ISBN) "
                + "VALUES ('"
                + reviews.getReviewID() + "', '"
                + reviews.getReviewDate() + "', '"
                + reviews.getBorrower().getBorrowerID() + "', '"
                + reviews.getReviewText() + "', '"
                + reviews.getRating() + "', '"
                + reviews.getBook().getISBN() + "')";
        int rows = stmt.executeUpdate(command);
        connection.close();
    }

    // Modify one record based on the given object
    @Override
    public void updateRecord(Object data) throws SQLException {
//          Reviews reviews = (Reviews) data;
//          connection = DriverManager.getConnection(
//                "jdbc:mysql://localhost:3306/library",
//                "root",
//                libraryController.getDBPassword());
//
//        Statement stmt = connection.createStatement();
//        String command =
//        stmt.executeUpdate(command);
//        connection.close();
    }

    // get one record, that matches the given value
    @Override
    public Object findOneRecord(String key) throws SQLException {
        Reviews reviews = new Reviews();

//        ResultSet rs;
//          connection = DriverManager.getConnection(
//                "jdbc:mysql://localhost:3306/library",
//                "root",
//                libraryController.getDBPassword());
//
//        // Create a Statement object
//        Statement stmt = connection.createStatement();
//        // Create a string with a SELECT statement
//        String command = "SELECT ";
//        // Execute the statement and return the result
//        rs = stmt.executeQuery(command);
//        while (rs.next()) {
//            // note that, this loop will run only once
//
//        }
//        connection.close();
        return reviews;
    }
    @Override
    public Object findOneRecord(String key1, String key2) throws SQLException {
        return null;
    }
    @Override
    public Object findOneRecord2(String key) throws SQLException {
        return null;
    }
    // Get a String list
    @Override
    public List<String> getKeys() throws SQLException {
        List<String> list = new ArrayList<>();
//        ResultSet rs;
//          connection = DriverManager.getConnection(
//                "jdbc:mysql://localhost:3306/library",
//                "root",
//                libraryController.getDBPassword());
//
//        // Create a Statement object
//        Statement stmt = connection.createStatement();
//
//        // Create a string with a SELECT statement
//        String command = "SELECT ";
//
//        // Execute the statement and return the result
//        rs = stmt.executeQuery(command);
//
//        while (rs.next()) {
//            list.add(rs.getString(1));
//        }
//        connection.close();
        return list;
    }

    @Override
    public void deleteOneRecord(String key) throws SQLException {
//          connection = DriverManager.getConnection(
//                "jdbc:mysql://localhost:3306/library",
//                "root",
//                libraryController.getDBPassword());
//        Statement stmt = connection.createStatement();
//        stmt.executeUpdate("DELETE ");
//        connection.close();
    }
    @Override
    public void deleteRecords(Object referencedObject) throws SQLException {

    }

    @Override
    public List<Object> getAllRecords() throws SQLException {
        return null;
    }

    @Override
    public List<Object> getAllRecords(Object referencedObject) throws SQLException {
        return null;
    }

    @Override
    public boolean isRegistered(String key) throws SQLException {
        return false;
    }
}
