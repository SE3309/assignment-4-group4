package se3309a.library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowingsTableAdapter implements DataStore {
    private Connection connection;
    LibraryController libraryController = new LibraryController();

    //private String DB_URL = "jdbc:mysql://localhost:3306/library";
    public BorrowingsTableAdapter(Boolean reset) throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());
        Statement stmt = connection.createStatement();
        if (reset) {
            try {
                // Remove tables if database tables have been created.
                // This will throw an exception if the tables do not exist
                stmt.execute("DROP TABLE borrowings");
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
            }
        }
        try {
            String command = "CREATE TABLE borrowings ("
                    + "borrowingID INT NOT NULL,"
                    + "borrowDate DATE NOT NULL,"
                    + "returnDate DATE,"
                    + "status ENUM('On Hold', 'Returned', 'Late') NOT NULL,"
                    + "borrowerID INT NOT NULL,"
                    + "ISBN CHAR(13) NOT NULL,"
                    + "fineID INT,"
                    + "FOREIGN KEY (borrowerID) REFERENCES borrower(borrowerID),"
                    + "FOREIGN KEY (ISBN) REFERENCES book(ISBN),"
                    + "FOREIGN KEY (fineID) REFERENCES fines(fineID),"
                    + "PRIMARY KEY (borrowingID)"
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
        Borrowings borrowings = (Borrowings) data;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());

        Statement stmt = connection.createStatement();
        if (borrowings.getFine() == null && borrowings.getReturnDate() != null) {
            String command = "INSERT INTO borrowings ( borrowingID, borrowDate, returnDate, status, borrowerID, ISBN, fineID) "
                    + "VALUES ('"
                    + borrowings.getBorrowingID() + "', '"
                    + borrowings.getBorrowDate() + "', '"
                    + borrowings.getReturnDate() + "', '"
                    + borrowings.getStatus() + "', '"
                    + borrowings.getBorrower().getBorrowerID() + "', '"
                    + borrowings.getBook().getISBN() + "', NULL)";
            int rows = stmt.executeUpdate(command);

        } else if (borrowings.getReturnDate() == null && borrowings.getFine() != null) {
            String command = "INSERT INTO borrowings ( borrowingID, borrowDate, returnDate, status, borrowerID, ISBN, fineID) "
                    + "VALUES ('"
                    + borrowings.getBorrowingID() + "', '"
                    + borrowings.getBorrowDate() + "', NULL, '"
                    + borrowings.getStatus() + "', '"
                    + borrowings.getBorrower().getBorrowerID() + "', '"
                    + borrowings.getBook().getISBN() + "', '"
                    + borrowings.getFine().getFineID() + "')";
            int rows = stmt.executeUpdate(command);
        } else if (borrowings.getReturnDate() == null && borrowings.getReturnDate() == null) {
            String command = "INSERT INTO borrowings ( borrowingID, borrowDate, returnDate, status, borrowerID, ISBN, fineID) "
                    + "VALUES ("
                    + borrowings.getBorrowingID() + ", '"
                    + borrowings.getBorrowDate() + "', NULL, '"
                    + borrowings.getStatus() + "', "
                    + borrowings.getBorrower().getBorrowerID() + ", '"
                    + borrowings.getBook().getISBN() + "', NULL)";
            System.out.println(command);
            int rows = stmt.executeUpdate(command);
        } else {
            String command = "INSERT INTO borrowings ( borrowingID, borrowDate, returnDate, status, borrowerID, ISBN, fineID) "
                    + "VALUES ('"
                    + borrowings.getBorrowingID() + "', '"
                    + borrowings.getBorrowDate() + "', '"
                    + borrowings.getReturnDate() + "', '"
                    + borrowings.getStatus() + "', '"
                    + borrowings.getBorrower().getBorrowerID() + "', '"
                    + borrowings.getBook().getISBN() + "', '"
                    + borrowings.getFine().getFineID() + "')";
            int rows = stmt.executeUpdate(command);

        }

        connection.close();
    }

    // Modify one record based on the given object
    @Override
    public void updateRecord(Object data) throws SQLException {
//                Borrowings borrowings = (Borrowings) data;
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
        Borrowings borrowings = new Borrowings();
        Borrower borrower = new Borrower();
        Book book = new Book();
        Fines fines = new Fines();

        borrowings.setFine(fines);
        borrowings.setBorrower(borrower);
        borrowings.setBook(book);

        ResultSet rs;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());

        // Create a Statement object
        Statement stmt = connection.createStatement();
        // Create a string with a SELECT statement
        String command = "SELECT * FROM borrowings WHERE borrowerId = '" + key + "'";
        // Execute the statement and return the result
        rs = stmt.executeQuery(command);
        while (rs.next()) {
            borrowings.setBorrowingID(rs.getInt("borrowingID"));
            borrowings.setBorrowDate(rs.getDate("borrowDate"));
            borrowings.setReturnDate(rs.getDate("returnDate"));
            borrowings.setStatus(rs.getString("status"));
            borrowings.getBorrower().setBorrowerID(rs.getInt("borrowerID"));
            borrowings.getBook().setISBN(rs.getString("ISBN"));
            borrowings.getFine().setFineID(rs.getInt("fineID"));
        }
        connection.close();
        return borrowings;
    }

    @Override
    public Object findOneRecord2(String key) throws SQLException {
        Borrowings borrowings = new Borrowings();
        Borrower borrower = new Borrower();
        Book book = new Book();
        Fines fines = new Fines();

        borrowings.setFine(fines);
        borrowings.setBorrower(borrower);
        borrowings.setBook(book);

        ResultSet rs;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());

        // Create a Statement object
        Statement stmt = connection.createStatement();
        // Create a string with a SELECT statement
        String command = "SELECT * FROM borrowings WHERE ISBN = '" + key + "'";
        // Execute the statement and return the result
        rs = stmt.executeQuery(command);
        while (rs.next()) {
            borrowings.setBorrowingID(rs.getInt("borrowingID"));
            borrowings.setBorrowDate(rs.getDate("borrowDate"));
            borrowings.setReturnDate(rs.getDate("returnDate"));
            borrowings.setStatus(rs.getString("status"));
            borrowings.getBorrower().setBorrowerID(rs.getInt("borrowerID"));
            borrowings.getBook().setISBN(rs.getString("ISBN"));
            borrowings.getFine().setFineID(rs.getInt("fineID"));
        }
        connection.close();
        return borrowings;
    }

    @Override
    public Object findOneRecord(String key1, String key2) throws SQLException {
        return null;
    }

    // Get a String list
    @Override
    public List<Integer> getKeys() throws SQLException {
        List<Integer> list = new ArrayList<>();
        ResultSet rs;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());

        // Create a Statement object
        Statement stmt = connection.createStatement();

        // Create a string with a SELECT statement
        String command = "SELECT borrowingID from borrowings ORDER BY borrowingID";


        // Execute the statement and return the result
        rs = stmt.executeQuery(command);
        while (rs.next()) {
            list.add(rs.getInt(1));
        }
        connection.close();
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

