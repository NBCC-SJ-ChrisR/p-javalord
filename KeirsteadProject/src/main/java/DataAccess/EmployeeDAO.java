package DataAccess;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Entity.Employee;
import DataAccess.Crypt;
import Entity.Toppings;

public class EmployeeDAO {
    private Connection conn = null;
    private PreparedStatement selectStatement = null;
    private String lastError = "";

    public String getLastError() { return lastError; }

    private boolean init() {
        if (conn != null) {
            return true;
        }
        conn = ConnectionManager.getConnection(ConnectionParameters.URL, ConnectionParameters.USERNAME, ConnectionParameters.PASSWORD);
        if (conn != null)
            try {
                selectStatement = conn.prepareStatement("SELECT * FROM employee WHERE username = ?");
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

    //Sent: String of username
    //Returned: Employee Object with hashed password
    //Desc: Receives username, then queries the database with username, if one matches it hashes the password and
    //      creates an employee object to return.
    public Employee get(String usernameIn) {
        lastError = "";
        Employee employee = null;

        if (!init()) {
            return null;
        }

        ResultSet rs;
        try {
            selectStatement.setString(1, usernameIn);
            rs = selectStatement.executeQuery();
        } catch (SQLException ex) {
            lastError = "Init:" + ex.getMessage();
            System.err.println("************************");
            System.err.println("** Error retreiving Employee");
            System.err.println("** " + ex.getMessage());
            System.err.println("************************");
            return null;
        }

        try {
            while (rs.next()) {
                int id = rs.getInt("employee_id");
                String username = rs.getString("username");
                String hashedPassword = rs.getString("password");
                employee = new Employee(id, username, hashedPassword);
            }
            System.err.println("*** get - found employee" + "USER:" + employee.getUsername() + employee.getPassword());

        } catch (SQLException ex) {
            lastError = "Init:" + ex.getMessage();
            System.err.println("************************");
            System.err.println("** Error populating Employees");
            System.err.println("** " + ex.getMessage());
            System.err.println("************************");
        }
        return employee;
    }

}
