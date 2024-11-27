package se3309a.library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowerTableAdapter implements DataStore{
    private Connection connection;
    LibraryController libraryController = new LibraryController();
    private String DB_URL = "jdbc:mysql://localhost:3306/library";


        public BorrowerTableAdapter(Boolean reset) throws SQLException {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library",
                    "root",
                    libraryController.getDBPassword());
            Statement stmt = connection.createStatement();

            if (reset) {
                try {
                    // Drop the borrower table if it exists
                    stmt.execute("DROP TABLE borrower");
                } catch (SQLException ex) {
                    // Ignore errors if the table doesn't exist
                }
            }

            try {
                // Create the borrower table
                String command = "CREATE TABLE borrower ("
                        + "borrowerID INT NOT NULL AUTO_INCREMENT,"
                        + "membershipDate DATE NOT NULL,"
                        + "bPassword VARCHAR(255) NOT NULL,"
                        + "bEmail VARCHAR(255) UNIQUE NOT NULL,"
                        + "PRIMARY KEY (borrowerID)"
                        + ")";
                stmt.execute(command);
            } catch (SQLException ex) {
                // Ignore errors if the table already exists
            }

            connection.close();
        }
    // Additional methods (insert, retrieve, update, delete, etc.)
    public void insertBorrower(Borrower borrower) throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());
        String command = "INSERT INTO borrower (membershipDate, bPassword, bEmail) VALUES (?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);

        stmt.setDate(1, borrower.getMembershipDate());
        stmt.setString(2, borrower.getbPassword());
        stmt.setString(3, borrower.getbEmail());
        stmt.executeUpdate();

        // Retrieve and set the generated borrowerID
        ResultSet keys = stmt.getGeneratedKeys();
        if (keys.next()) {
            borrower.setBorrowerID(keys.getInt(1));
        }

        stmt.close();
        connection.close();
    }


    public boolean isRegistered(String key) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM borrower WHERE bEmail = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", libraryController.getDBPassword());
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, key);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count") > 0; // Email exists if count > 0
            }
        }
        return false; // Email does not exist
    }

    public Borrower getBorrower(int borrowerID) throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());
        String command = "SELECT * FROM borrower WHERE borrowerID = ?";
        PreparedStatement stmt = connection.prepareStatement(command);

        stmt.setInt(1, borrowerID);
        ResultSet rs = stmt.executeQuery();

        Borrower borrower = null;
        if (rs.next()) {
            borrower = new Borrower();
            borrower.setBorrowerID(rs.getInt("borrowerID"));
            borrower.setMembershipDate(rs.getDate("membershipDate"));
            borrower.setbPassword(rs.getString("bPassword"));
            borrower.setbEmail(rs.getString("bEmail"));
        }

        rs.close();
        stmt.close();
        connection.close();

        return borrower;
    }

    // adds new record
        @Override
    public void addNewRecord(Object data) throws SQLException {
        Borrower borrower = (Borrower) data;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());

        Statement stmt = connection.createStatement();
        String command = "INSERT INTO borrower ( borrowerID, membershipDate, bPassword, bEmail) "
                + "VALUES ('"
                + borrower.getBorrowerID() + "', '"
                + borrower.getMembershipDate() + "', '"
                + borrower.getbPassword() + "', '"
                + borrower.getbEmail() + "')";
        int rows = stmt.executeUpdate(command);
        connection.close();
    }

    // Modify one record based on the given object
    @Override
    public void updateRecord(Object data) throws SQLException {
        Borrower borrower = (Borrower) data;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());

        Statement stmt = connection.createStatement();
        String command = "UPDATE borrower SET "
                + "borrowerID = '" + borrower.getBorrowerID() + "', "
                + "membershipDate = '" + borrower.getMembershipDate() + "', "
                + "bPassword = '" + borrower.getbPassword() + "', "
                + "bEmail = '" + borrower.getbEmail() + "' "
                + "WHERE borrowerID = '" + borrower.getBorrowerID() + "'";
        stmt.executeUpdate(command);
        connection.close();
    }

    // get one record, that matches the given value
    @Override
    public Object findOneRecord(String key) throws SQLException {
        Borrower borrower = new Borrower();
        ResultSet rs;
          connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());

        // Create a Statement object
        Statement stmt = connection.createStatement();
        // Create a string with a SELECT statement
        String command = "SELECT * FROM borrower WHERE bEmail = '" + key +"'";
        // Execute the statement and return the result
        rs = stmt.executeQuery(command);
        while (rs.next()) {
            borrower.setBorrowerID(rs.getInt("borrowerID"));
            borrower.setMembershipDate(rs.getDate("membershipDate"));
            borrower.setbEmail(rs.getString("bEmail"));
            borrower.setbPassword(rs.getString("bPassword"));

        }
        connection.close();
        return borrower;
    }

    @Override
    public Object findOneRecord2(String key) throws SQLException {
        Borrower borrower = new Borrower();
        ResultSet rs;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());

        // Create a Statement object
        Statement stmt = connection.createStatement();
        // Create a string with a SELECT statement
        String command = "SELECT * FROM borrower WHERE borrowerID = '" + key +"'";
        // Execute the statement and return the result
        rs = stmt.executeQuery(command);
        while (rs.next()) {
            borrower.setBorrowerID(rs.getInt("borrowerID"));
            borrower.setMembershipDate(rs.getDate("membershipDate"));
            borrower.setbEmail(rs.getString("bEmail"));
            borrower.setbPassword(rs.getString("bPassword"));

        }
        connection.close();
        return borrower;
    }
    @Override
    public Object findOneRecord(String key1, String key2) throws SQLException {
        Borrower borrower = new Borrower();

        ResultSet rs;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());

        // Create a Statement object
        Statement stmt = connection.createStatement();
        // Create a string with a SELECT statement
        String command = "SELECT * FROM borrower WHERE bEmail = '" + key1 + "' AND bPassword = '" + key2 + "'";
        // Execute the statement and return the result
        rs = stmt.executeQuery(command);
        while (rs.next()) {
                borrower.setBorrowerID(rs.getInt("borrowerID"));
            borrower.setMembershipDate(rs.getDate("membershipDate"));
            borrower.setbEmail(rs.getString("bEmail"));
            borrower.setbPassword(rs.getString("bPassword"));

        }
        connection.close();
        return borrower;    }

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
        String command = "SELECT borrowerID from borrower ORDER BY borrowerID";

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
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library",
                "root",
                libraryController.getDBPassword());
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM borrower WHERE bEmail = '"
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
        return null;
    }
    @Override
    public List<Object> getAllRecords(String referencedObject, String referencedObject2, String referenceObject3) throws SQLException {
        return null;
    }


}
