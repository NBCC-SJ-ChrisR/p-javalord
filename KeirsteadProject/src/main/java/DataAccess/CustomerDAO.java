package DataAccess;


import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Entity.Customer;
import DataAccess.Crypt;
import Entity.Toppings;

public class CustomerDAO {
    private Connection conn = null;
    private PreparedStatement insertStatement = null;
    private String lastError = "";

    public String getLastError() { return lastError; }

    private boolean init() {
        if (conn != null) {
            return true;
        }
        conn = ConnectionManager.getConnection(ConnectionParameters.URL, ConnectionParameters.USERNAME, ConnectionParameters.PASSWORD);
        if (conn != null)
            try {
                insertStatement = conn.prepareStatement("INSERT INTO customer (firstName, lastName, phoneNumber, email, houseNumber, street, province, postalCode) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                return true;
            } catch (SQLException ex) {
                lastError = "Init:" + ex.getMessage();
                System.err.println("************************");
                System.err.println("** Error preparing SQL");
                System.err.println("** " + ex.getMessage());
                System.err.println("************************");
                lastError = ex.getMessage();
                conn = null;
            }
        return false;
    }

    // if successful, the Student ID is set to the value from the Database
    public boolean insert(Customer c) {
        lastError = "";
        if (!init()) {
            return false;
        }
        System.err.println("inserting ...");
        ResultSet rs = null;
        try {
            insertStatement.setString(1, c.getFirstName());
            insertStatement.setString(2, c.getLastName());
            insertStatement.setString(3, c.getPhone());
            insertStatement.setString(4, c.getEmail());
            insertStatement.setInt(5, c.getHouseNumber());
            insertStatement.setString(6, c.getStreet());
            insertStatement.setString(7, c.getProvince());
            insertStatement.setString(8, c.getPostalCode());
            int rowCount = insertStatement.executeUpdate();
            System.out.println("DataAccess.CustomerDAO.insert() rows=" + rowCount);
            if (rowCount != 1) {
                return false;
            }

            // now that the new record has been created,
            // we need to retrieve the AUTO_INCREMENT Employee ID
            rs = insertStatement.getGeneratedKeys();
            if (rs.next()) {
                c.setId(rs.getInt(1));
                System.out.println("DataAccess.Customer.insert() id=" + c.getId());
            }

            return true;
        } catch (SQLException ex) {
            lastError = "Init:" + ex.getMessage();
            System.err.println("************************");
            System.err.println("** Error inserting Employee");
            System.err.println(ex.getMessage());
            System.err.println("************************");
            return false;
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException ex) {
                    // SQUELCH
                }
        }
    }

}
