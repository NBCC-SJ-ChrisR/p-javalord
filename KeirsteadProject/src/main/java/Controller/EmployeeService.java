package Controller;

import DataAccess.EmployeeDAO;
import com.google.gson.Gson;
import DataAccess.EmployeeDAO;
import Entity.Employee;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import DataAccess.Crypt;
import java.util.Map;

@WebServlet(name = "EmployeeService", urlPatterns = {"/EmployeeService/employees", "/EmployeeService/employees/*"})
public class EmployeeService extends HttpServlet {
    public EmployeeService() {
        System.out.println("EmployeeService constructor");
    }

    private EmployeeDAO getDAO(HttpServletRequest request) {
        HttpSession session = request.getSession();
        EmployeeDAO dao = (EmployeeDAO) session.getAttribute("employeeDAO");
        if (dao == null) {
            dao = new EmployeeDAO();
            session.setAttribute("employeeDAO", dao);
        }
        return dao;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EmployeeDAO dao = getDAO(request);
        HttpSession session = request.getSession();
        //Clears session data is username already exists
        if (session.getAttribute("username") != null) {
            session.invalidate();
        }
        try (PrintWriter out = response.getWriter()) {
            Scanner scanner = new Scanner(request.getReader());
            String jsonData = scanner.nextLine();

            Gson gson = new Gson();
            Employee e = gson.fromJson(jsonData, Employee.class);
            Employee e2 = dao.get(e.getUsername());
            if (e.checkMatch(e, e2)) {
                System.out.println("IN USER1: " + e.getUsername() + "IN PASS: " + e.getPassword());
                System.out.println("DB USER: " + e2.getUsername() + "DB PASS: " + e2.getPassword());
                session.setAttribute("username", e2.getId());
                Map<String, Object> data = new HashMap<>();
                data.put("valid", true);
                out.println(gson.toJson(data));
            } else if (!e.checkMatch(e, e2)) {
                Map<String, Object> data = new HashMap<>();
                data.put("invalid", true);
                out.println(gson.toJson(data));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                Map<String, Object> data = new HashMap<>();
                data.put("error", dao.getLastError());
                out.println(gson.toJson(data));
            }
        }
    }

}
