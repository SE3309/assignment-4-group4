package se3309a.library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BBorrowingsTableAdapter implements DataStore {
    private Connection connection;
    LibraryController libraryController = new LibraryController();

    // private String DB_URL = "jdbc:derby:libraryDB";
    public BBorrowingsTableAdapter(Boolean reset) throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());
        Statement stmt = connection.createStatement();
        if (reset) {
            try {
                // Remove tables if database tables have been created.
                // This will throw an exception if the tables do not exist
                stmt.execute("DROP TABLE bBorrowings");
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
            }
        }
        try {
            String command = "CREATE TABLE bBorrowings ("
                    + "borrowerID int NOT NULL,"
                    + "borrowingID int NOT NULL,"
                    + "FOREIGN KEY (borrowerID) REFERENCES borrower(borrowerID),"
                    + "FOREIGN KEY (borrowingID) REFERENCES borrowings(borrowingID),"
                    + "PRIMARY KEY (borrowerID)"
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
        BBorrowings bBorrowings = (BBorrowings) data;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());

        Statement stmt = connection.createStatement();
        String foreignCheck = "SET FOREIGN_KEY_CHECKS=0";
        stmt.executeUpdate(foreignCheck);

        String command = "INSERT INTO bBorrowings ( borrowerID, borrowingID) "
                + "VALUES ("
                + bBorrowings.getBorrower().getBorrowerID() + ", "
                + bBorrowings.getBorrowings().getBorrowingID() + ")";
        System.out.println(command);
        int rows = stmt.executeUpdate(command);

        String foreignCheck2 = "SET FOREIGN_KEY_CHECKS=1";
        stmt.executeUpdate(foreignCheck2);
        connection.close();
    }

    // Modify one record based on the given object
    @Override
    public void updateRecord(Object data) throws SQLException {
//        BBorrowings bBorrowings = (BBorrowings) data;
//         connection = DriverManager.getConnection(
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
        BBorrowings bBorrowings = new BBorrowings();
        Borrower borrower = new Borrower();
        Borrowings borrowings = new Borrowings();

        bBorrowings.setBorrower(borrower);
        bBorrowings.setBorrowings(borrowings);

        ResultSet rs;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());

        // Create a Statement object
        Statement stmt = connection.createStatement();
        // Create a string with a SELECT statement
        String command = "SELECT * FROM bBorrowings WHERE borrowerID = '" + key + "'";
        // Execute the statement and return the result
        rs = stmt.executeQuery(command);
        while (rs.next()) {
            bBorrowings.getBorrower().setBorrowerID(rs.getInt("borrowerID"));
            bBorrowings.getBorrowings().setBorrowingID(rs.getInt("borrowingID"));

        }
        connection.close();
        return bBorrowings;
    }

    @Override
    public Object findOneRecord2(String key) throws SQLException {
        int duration = 0;

        ResultSet rs;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());

        // Create a Statement object
        Statement stmt = connection.createStatement();
        // Create a string with a SELECT statement
        String command = "SELECT DATEDIFF(returnDate, borrowDate) AS borrowDuration " +
                "FROM borrowings " +
                "WHERE ISBN = '" + key + "'" +
                "AND returnDate IS NOT NULL";

        // Execute the statement and return the result
        rs = stmt.executeQuery(command);
        while (rs.next()) {
            duration = rs.getInt("borrowDuration");
        }
        connection.close();
        return duration;
    }

    @Override
    public Object findOneRecord(String key1, String key2) throws SQLException {
        return null;
    }

    @Override
    public boolean isRegistered(String key) throws SQLException {
        return false;
    }

    // Get a String list
    @Override
    public List<Integer> getKeys() throws SQLException {
        List<Integer> list = new ArrayList<>();
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
//        String command = "SELECT COUNT(borrowerID) " +
//                "FROM bBorrowings";
//
//        // Execute the statement and return the result
//        rs = stmt.executeQuery(command);
//
//
//        connection.close();
        return list;
    }

    @Override
    public void deleteOneRecord(String key) throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM bBorrowings WHERE borrowerID = '"
                + key + "'");
        connection.close();
    }

    @Override
    public void deleteRecords(Object referencedObject) throws SQLException {

    }

    @Override
    public List<Object> getAllRecords() throws SQLException {
        List<Object> list = new ArrayList<>();
        ResultSet result;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library",
                    "root",
                    libraryController.getDBPassword());

            // Create a Statement object
            Statement stmt = connection.createStatement();
            //  Create a string with a SELECT statement
            String command = "SELECT borrowerID, COUNT(*) AS borrowCount FROM borrowings GROUP BY borrowerID";

            // Execute the statement and return the result
            result = stmt.executeQuery(command);
            while (result.next()) {
                Borrowings borrowings = new Borrowings();
                Borrower borrower = new Borrower();
                borrowings.setBorrower(borrower);
                borrowings.setBorrowCount(result.getInt("borrowCount"));
                borrowings.getBorrower().setBorrowerID(result.getInt("borrowerID"));

                list.add(borrowings);
            }
            connection.close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }    }

    @Override
    public List<Object> getAllRecords(String referencedObject) throws SQLException {
        return null;
    }

    @Override
    public List<Object> getAllRecords(String referencedObject, String referencedObject2, String referenceObject3) throws SQLException {
        return null;
    }
}
