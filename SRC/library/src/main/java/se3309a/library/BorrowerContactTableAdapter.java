package se3309a.library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowerContactTableAdapter implements DataStore{
    private Connection connection;


    LibraryController libraryController = new LibraryController();
    //private String DB_URL = "jdbc:mysql://localhost:3306/library";
    public BorrowerContactTableAdapter(Boolean reset) throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());
        Statement stmt = connection.createStatement();
        if (reset) {
            try {
                // Remove tables if database tables have been created.
                // This will throw an exception if the tables do not exist
                stmt.execute("DROP TABLE borrowerContact");
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
            }
        }
        try {
            String command = "CREATE TABLE borrowerContact ("
                    + "bEmail VARCHAR(255) UNIQUE NOT NULL,"
                    + "bName VARCHAR(255) NOT NULL,"
                    + "FOREIGN KEY (bEmail) REFERENCES borrower(bEmail),"
                    + "PRIMARY KEY (bEmail)"
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
        BorrowerContact borrowerContact = (BorrowerContact) data;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());

        Statement stmt = connection.createStatement();
        String command = "INSERT INTO borrowerContact ( bEmail, bName) "
                + "VALUES ('"
                + borrowerContact.getBorrower().getbEmail() + "', '"
                + borrowerContact.getName() + "')";
        int rows = stmt.executeUpdate(command);
        connection.close();
    }

    // Modify one record based on the given object
    @Override
    public void updateRecord(Object data) throws SQLException {
//        BorrowerContact borrowerContact = (BorrowerContact) data;
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
        BorrowerContact borrowerContact = new BorrowerContact();
        Borrower borrower = new Borrower();
        borrowerContact.setBorrower(borrower);
        ResultSet rs;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());

        // Create a Statement object
        Statement stmt = connection.createStatement();
        // Create a string with a SELECT statement
        String command = "SELECT * FROM borrowerContact WHERE bEmail = '" + key +"'";
        // Execute the statement and return the result
        rs = stmt.executeQuery(command);
        while (rs.next()) {
            borrowerContact.getBorrower().setbEmail(rs.getString("bEmail"));
            borrowerContact.setName(rs.getString("bName"));
        }
        connection.close();
        return borrowerContact;
    }
    @Override
    public Object findOneRecord(String key1, String key2) throws SQLException {
        return null;
    }

    @Override
    public Object findOneRecord2(String key) throws SQLException {
        BorrowerContact borrowerContact = new BorrowerContact();
        Borrower borrower = new Borrower();
        borrowerContact.setBorrower(borrower);
        ResultSet rs;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());

        // Create a Statement object
        Statement stmt = connection.createStatement();
        // Create a string with a SELECT statement
        String command = "SELECT bName FROM borrowerContact WHERE bEmail = " +
                "(SELECT bEmail FROM borrower WHERE borrowerID = " +
                "(SELECT borrowerID FROM borrowings WHERE ISBN = '" + key +"'))";
        // Execute the statement and return the result
        rs = stmt.executeQuery(command);
        while (rs.next()) {
            borrowerContact.setName(rs.getString("bName"));
        }
        connection.close();
        return borrowerContact;    }

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
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM borrowerContact WHERE bEmail = '"
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
            String command = "SELECT * FROM borrowerContact";

            // Execute the statement and return the result
            result = stmt.executeQuery(command);
            while (result.next()) {
                BorrowerContact borrowerContact = new BorrowerContact();
                Borrower borrower = new Borrower();
                borrowerContact.setBorrower(borrower);
                borrowerContact.setName(result.getString("bName"));
                borrowerContact.getBorrower().setbEmail(result.getString("bEmail"));

                list.add(borrowerContact);
            }
            connection.close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Object> getAllRecords(String referencedObject) throws SQLException {
        return null;
    }

    @Override
    public boolean isRegistered(String key) throws SQLException {
        return false;
    }
    @Override
    public List<Object> getAllRecords(String referencedObject, String referencedObject2, String referenceObject3) throws SQLException {
        return null;
    }
}
