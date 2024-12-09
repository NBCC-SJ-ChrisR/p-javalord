package DataAccess;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import Entity.PizzaCrust;
import Entity.Toppings;

public class PizzaCrustDAO {
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
                selectAllStatement = conn.prepareStatement("SELECT * FROM pizzacrust");
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
    public List<PizzaCrust> getAll() {
        lastError = "";
        List<PizzaCrust> list = new ArrayList();
        if (!init()) {
            return list;
        }

        ResultSet rs;
        try {
            rs = selectAllStatement.executeQuery();
        } catch (SQLException ex) {
            lastError = "Init:" + ex.getMessage();
            System.err.println("************************");
            System.err.println("** Error retreiving Crusts from DB");
            System.err.println("** " + ex.getMessage());
            System.err.println("************************");
            return list;
        }

        try {
            while (rs.next()) {
                int id = rs.getInt("pizzaCrust_id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                list.add(new PizzaCrust(id, name, price));
            }
            System.err.println("*** getAll() - found " + list.size() + " crusts");

        } catch (SQLException ex) {
            lastError = "Init:" + ex.getMessage();
            System.err.println("************************");
            System.err.println("** Error populating Crust");
            System.err.println("** " + ex.getMessage());
            System.err.println("************************");
        }
        return list;
    }


}
