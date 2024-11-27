package se3309a.library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookBorrowingsTableAdapter implements DataStore{
    private Connection connection;
    LibraryController libraryController = new LibraryController();
   // private String DB_URL = "jdbc:derby:libraryDB";
    public BookBorrowingsTableAdapter(Boolean reset) throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());
        Statement stmt = connection.createStatement();
        if (reset) {
            try {
                // Remove tables if database tables have been created.
                // This will throw an exception if the tables do not exist
                stmt.execute("DROP TABLE bookBorrowings");
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
            }
        }
        try {
            String command = "CREATE TABLE bookBorrowings ("
                    + "ISBN CHAR(13) UNIQUE NOT NULL,"
                    + "borrowingID INT UNIQUE NOT NULL,"
                    + "FOREIGN KEY (ISBN) REFERENCES book(ISBN),"
                    + "FOREIGN KEY (borrowingID) REFERENCES borrowings(borrowingID),"
                    + "PRIMARY KEY (ISBN)"
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
        BookBorrowings bookBorrowings = (BookBorrowings) data;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());

        Statement stmt = connection.createStatement();
        String command = "INSERT INTO bookBorrowings ( ISBN, borrowingID) "
                + "VALUES ('"
                + bookBorrowings.getBook().getISBN() + "', '"
                + bookBorrowings.getBorrowings().getBorrowingID() + "')";
        System.out.println(command);

        int rows = stmt.executeUpdate(command);
        connection.close();
    }

    // Modify one record based on the given object
    @Override
    public void updateRecord(Object data) throws SQLException {
//       BookBorrowings bookBorrowings = (BookBorrowings) data;
//        connection = DriverManager.getConnection(
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
        BookBorrowings bookBorrowings = new BookBorrowings();
        Borrowings borrowings = new Borrowings();
        Book book = new Book();
        bookBorrowings.setBorrowings(borrowings);
        bookBorrowings.setBook(book);
        ResultSet rs;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());

        // Create a Statement object
        Statement stmt = connection.createStatement();
        // Create a string with a SELECT statement
        String command = "SELECT * FROM bookBorrowings WHERE borrowingID = '" + key + "'";
        // Execute the statement and return the result
        rs = stmt.executeQuery(command);
        while (rs.next()) {
            bookBorrowings.getBorrowings().setBorrowingID(rs.getInt("borrowingID"));
            bookBorrowings.getBook().setISBN(rs.getString("ISBN"));

        }
        connection.close();
        return bookBorrowings;
    }

    @Override
    public Object findOneRecord2(String key) throws SQLException {
        return null;
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
//        connection = DriverManager.getConnection(
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
        stmt.executeUpdate("DELETE FROM bookBorrowings WHERE borrowingID = '"
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

            // Create a string with a SELECT statement
            String command = "SELECT b.ISBN, b.title, a.author " +
                    "FROM book b JOIN bookAuthor a ON b.ISBN = a.ISBN";

            // Execute the statement and return the result
            result = stmt.executeQuery(command);

            while (result.next()) {
                Book book = new Book();
                book.setISBN(result.getString("ISBN"));
                book.setTitle(result.getString("title"));
                book.setAuthor(result.getString("author"));

                list.add(book);
            }
            connection.close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
            String command = "SELECT b.ISBN, b.title, a.author FROM book b " +
                    "JOIN bookAuthor a ON b.ISBN = a.ISBN " +
                    "WHERE LOWER(a.author) LIKE '"
                    + referencedObject + "'";

            // Execute the statement and return the result
            result = stmt.executeQuery(command);

            while (result.next()) {
                Book book = new Book();
                book.setISBN(result.getString("ISBN"));
                book.setTitle(result.getString("title"));
                book.setAuthor(result.getString("author"));

                list.add(book);
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
            String command = "SELECT b.ISBN, b.title, a.author " +
                    "FROM book b JOIN bookAuthor a ON b.ISBN = a.ISBN " +
                    "WHERE LOWER(b.title) LIKE '"
                    + referencedObject + "'";

            // Execute the statement and return the result
            result = stmt.executeQuery(command);

            while (result.next()) {
                Book book = new Book();
                book.setISBN(result.getString("ISBN"));
                book.setTitle(result.getString("title"));
                book.setAuthor(result.getString("author"));

                list.add(book);
            }
            connection.close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }    }
}
