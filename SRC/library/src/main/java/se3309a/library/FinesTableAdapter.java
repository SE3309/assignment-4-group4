package se3309a.library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FinesTableAdapter implements DataStore{
    private Connection connection;
    LibraryController libraryController = new LibraryController();
    //private String DB_URL = "jdbc:mysql://localhost:3306/library";
    public FinesTableAdapter(Boolean reset) throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());
        Statement stmt = connection.createStatement();
        if (reset) {
            try {
                // Remove tables if database tables have been created.
                // This will throw an exception if the tables do not exist
                stmt.execute("DROP TABLE fines");
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
            }
        }
        try {
            String command = "CREATE TABLE fines ("
                    + "fineID INT NOT NULL AUTO_INCREMENT,"
                    + "borrowerID INT NOT NULL,"
                    + "dueDate DATE NOT NULL,"
                    + "datePaid DATE,"
                    + "FOREIGN KEY (borrowerID) REFERENCES borrower(borrowerID),"
                    + "PRIMARY KEY (fineID)"
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
        Fines fines = (Fines) data;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());

        Statement stmt = connection.createStatement();
        String command = "INSERT INTO fines ( fineID, borrowerID, dueDate, datePaid) "
                + "VALUES ('"
                + fines.getFineID() + "', '"
                + fines.getBorrower().getBorrowerID()+ "', '"
                + fines.getDueDate() + "', '"
                + fines.getDatePaid() + "')";
        int rows = stmt.executeUpdate(command);
        connection.close();
    }

    // Modify one record based on the given object
    @Override
    public void updateRecord(Object data) throws SQLException {
//          Fines fines = (Fines) data;
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
        Fines fines = new Fines();
        Borrower borrower = new Borrower();

        fines.setBorrower(borrower);

        ResultSet rs;
          connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());

        // Create a Statement object
        Statement stmt = connection.createStatement();
        // Create a string with a SELECT statement
        String command = "SELECT * FROM fines WHERE borrowerId = '" + key +"'";
        // Execute the statement and return the result
        rs = stmt.executeQuery(command);
        while (rs.next()) {
            fines.setFineID(rs.getInt("fineID"));
            fines.getBorrower().setBorrowerID(rs.getInt("borrowerID"));
            fines.setDueDate(rs.getDate("dueDate"));
            fines.setDatePaid(rs.getDate("datePaid"));
        }
        connection.close();
        return fines;
    }

    @Override
    public Object findOneRecord2(String key) throws SQLException {
        Fines fines = new Fines();
//
//        ResultSet rs;
//        connection = DriverManager.getConnection(
//                "jdbc:mysql://localhost:3306/library",
//                "root",
//                libraryController.getDBPassword());
//
//        // Create a Statement object
//        Statement stmt = connection.createStatement();
//        // Create a string with a SELECT statement
//        String command = "SELECT fineId, borrowerId, dueDate, datePaid FROM fines WHERE borrowerId = '" + key +"'";
//        // Execute the statement and return the result
//        rs = stmt.executeQuery(command);
//        while (rs.next()) {
//            // note that, this loop will run only once
//
//        }
//        connection.close();
        return fines;
    }
    @Override
    public Object findOneRecord(String key1, String key2) throws SQLException {
        return null;
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
        stmt.executeUpdate("DELETE FROM fines WHERE borrowerID = '"
                + key + "'");
        connection.close();
    }
    @Override
    public void deleteRecords(Object referencedObject) throws SQLException {

    }

    @Override
    public List<Object> getAllRecords() throws SQLException {
        return null;
    }

    @Override
    public List<Object> getAllRecords(String referencedObject) throws SQLException {
        List<Object> list = new ArrayList<>();
        ResultSet result;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library",
                    "root",
                    libraryController.getDBPassword());

            // Create a Statement object
            Statement stmt = connection.createStatement();

            // Create a string with a SELECT statement
            String command = "SELECT * FROM fines WHERE borrowerID = '"
                    + referencedObject + "' ORDER BY fineID";
            // Execute the statement and return the result
            result = stmt.executeQuery(command);

            while (result.next()) {
                Fines fines = new Fines();
                Borrower borrower = new Borrower();
                fines.setBorrower(borrower);
                fines.setFineID(result.getInt("fineID"));
                fines.getBorrower().setBorrowerID(result.getInt("borrowerID"));
                fines.setDueDate(result.getDate("dueDate"));
                fines.setDatePaid(result.getDate("datePaid"));


                list.add(fines);
            }
            connection.close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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