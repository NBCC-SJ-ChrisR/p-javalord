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
import Entity.Orders;
import Entity.Toppings;

public class OrdersDAO {
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
                selectAllStatement = conn.prepareStatement("SELECT * FROM orders");
                deleteStatement = conn.prepareStatement("DELETE FROM orders WHERE order_id = ?");
                insertStatement = conn.prepareStatement("INSERT INTO orders (customer_id, subTotal, hst, orderTotal, orderStatus, deliveryDate, " +
                        "orderPlacedDate) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                updateStatement = conn.prepareStatement("UPDATE orders SET orderStatus = ? WHERE order_id = ?");
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
    public List<Orders> getAll() {
        lastError = "";
        List<Orders> list = new ArrayList();
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
                int id = rs.getInt("order_id");
                int customerId = rs.getInt("customer_id");
                double subtotal = rs.getDouble("subTotal");
                double hst = rs.getDouble("hst");
                double orderTotal = rs.getDouble("orderTotal");
                String status = rs.getString("orderStatus");
                Timestamp deliveryDate = rs.getTimestamp("deliveryDate");
                Timestamp orderPlacedDate = rs.getTimestamp("orderPlacedDate");
                list.add(new Orders(id, customerId, subtotal, hst, orderTotal, status, deliveryDate, orderPlacedDate));
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

    //"UPDATE orders SET orderStatus = ? WHERE order_id = ?"
    public boolean update(Orders o) {
        lastError = "";
        try {
            updateStatement.setString(1, "FILLED");
            updateStatement.setInt(2, o.getId());
            int rowCount = updateStatement.executeUpdate();
            if (rowCount != 1) {
                System.out.println("DataAccess.OrdersDAO.update() rows=" + rowCount);
                return false;
            }

            return true;
        } catch (SQLException ex) {
            lastError = "Init:" + ex.getMessage();
            System.err.println("************************");
            System.err.println("** Error updating Employee");
            System.err.println(ex.getMessage());
            System.err.println("************************");
            return false;
        }
    }
}
