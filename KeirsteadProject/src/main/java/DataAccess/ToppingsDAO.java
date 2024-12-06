package DataAccess;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import Entity.Employee;
import Entity.Toppings;

public class ToppingsDAO {
    private Connection conn = null;
    private PreparedStatement selectAllStatement = null;
    private PreparedStatement insertStatement = null;
    private PreparedStatement updateStatement = null;
    private PreparedStatement deleteStatement = null;
    private String lastError = "";

    public String getLastError() { return lastError; }

    private boolean init() {
        if (conn != null) {
            return true;
        }
        conn = ConnectionManager.getConnection(ConnectionParameters.URL, ConnectionParameters.USERNAME, ConnectionParameters.PASSWORD);
        if (conn != null)
            try {
                selectAllStatement = conn.prepareStatement("SELECT * FROM pizzatopping");
                deleteStatement = conn.prepareStatement("DELETE FROM pizzatopping WHERE pizzaTopping_id = ?");
                insertStatement = conn.prepareStatement("INSERT INTO pizzatopping (name, price, createdate, empAddedBy, isActive) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
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

    // Add
    public boolean insert(Toppings t) {
        lastError = "";
        if (!init()) {
            return false;
        }
        System.err.println("inserting ...");
        ResultSet rs = null;
        try {
            insertStatement.setString(1, t.getName());
            insertStatement.setDouble(2, t.getPrice());
            insertStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            insertStatement.setInt(4, t.getEmpAdd());
            insertStatement.setInt(5, t.isActive());
            int rowCount = insertStatement.executeUpdate();
            System.out.println("DataAccess.StudentDAO.insert() rows=" + rowCount);
            if (rowCount != 1) {
                return false;
            }
            // now that the new record has been created,
            // we need to retrieve the AUTO_INCREMENT Employee ID
            rs = insertStatement.getGeneratedKeys();
            if (rs.next()) {
                t.setId(rs.getInt(1));
                System.out.println("DataAccess.EmployeeDAO.insert() id=" + t.getId());
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
                    lastError += "Last Error:" + ex.getMessage();
                }
        }
    }

    //Delete
    public boolean delete(Toppings topping) {
        lastError = "";
        try {
            deleteStatement.setInt(1, topping.getId());
            int rowCount = deleteStatement.executeUpdate();
            if (rowCount != 1) {
                System.out.println("DataAccess.EmployeeDAO.delete() rows=" + rowCount);
                return false;
            }

            return true;
        } catch (SQLException ex) {
            lastError = "Init:" + ex.getMessage();
            System.err.println("************************");
            System.err.println("** Error deleting Employee");
            System.err.println(ex.getMessage());
            System.err.println("************************");
            return false;
        }
    }

    //Get ALL
    public List<Toppings> getAll() {
        lastError = "";
        List<Toppings> list = new ArrayList();
        if (!init()) {
            return list;
        }

        ResultSet rs;
        try {
            rs = selectAllStatement.executeQuery();
        } catch (SQLException ex) {
            lastError = "Init:" + ex.getMessage();
            System.err.println("************************");
            System.err.println("** Error retreiving Toppings from DB");
            System.err.println("** " + ex.getMessage());
            System.err.println("************************");
            return list;
        }

        try {
            while (rs.next()) {
                int id = rs.getInt("pizzaTopping_id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int isActive = rs.getInt("isActive");
                list.add(new Toppings(id, name, price, Timestamp.valueOf(LocalDateTime.now()), 1, isActive));
            }
            System.err.println("*** getAll() - found " + list.size() + " toppings");

        } catch (SQLException ex) {
            lastError = "Init:" + ex.getMessage();
            System.err.println("************************");
            System.err.println("** Error populating Toppings");
            System.err.println("** " + ex.getMessage());
            System.err.println("************************");
        }
        return list;
    }
}
