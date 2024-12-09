package DataAccess;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Entity.PizzaCrust;
import Entity.PizzaSize;

public class PizzaSizeDAO {
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
                selectAllStatement = conn.prepareStatement("SELECT * FROM pizzasize");
                //deleteStatement = conn.prepareStatement("DELETE FROM pizzatopping WHERE pizzaTopping_id = ?");
                //insertStatement = conn.prepareStatement("INSERT INTO pizzatopping (name, price, createdate, empAddedBy, isActive) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
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

    //Get ALL
    public List<PizzaSize> getAll() {
        lastError = "";
        List<PizzaSize> list = new ArrayList();
        if (!init()) {
            return list;
        }

        ResultSet rs;
        try {
            rs = selectAllStatement.executeQuery();
        } catch (SQLException ex) {
            lastError = "Init:" + ex.getMessage();
            System.err.println("************************");
            System.err.println("** Error retreiving Sizes from DB");
            System.err.println("** " + ex.getMessage());
            System.err.println("************************");
            return list;
        }

        try {
            while (rs.next()) {
                int id = rs.getInt("pizzaSize_id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                list.add(new PizzaSize(id, name, price));
            }
            System.err.println("*** getAll() - found " + list.size() + " sizes");

        } catch (SQLException ex) {
            lastError = "Init:" + ex.getMessage();
            System.err.println("************************");
            System.err.println("** Error populating Size");
            System.err.println("** " + ex.getMessage());
            System.err.println("************************");
        }
        return list;
    }
}
