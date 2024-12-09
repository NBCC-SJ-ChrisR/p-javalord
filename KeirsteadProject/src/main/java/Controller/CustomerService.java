package Controller;

import DataAccess.CustomerDAO;
import DataAccess.EmployeeDAO;
import com.google.gson.Gson;
import Entity.Customer;
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

@WebServlet(name = "CustomerService", urlPatterns = {"/CustomerService/customer", "/CustomerService/customer/*"})
public class CustomerService extends HttpServlet {
    public CustomerService() {
        System.out.println("CustomerService constructor");
    }

    private CustomerDAO getDAO(HttpServletRequest request) {
        HttpSession session = request.getSession();
        CustomerDAO dao = (CustomerDAO) session.getAttribute("customerDAO");
        if (dao == null) {
            dao = new CustomerDAO();
            session.setAttribute("customerDAO", dao);
        }
        return dao;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CustomerDAO dao = getDAO(request);

        try (PrintWriter out = response.getWriter()) {
            Scanner sc = new Scanner(request.getReader());
            String jsonData = sc.nextLine();

            Gson g = new Gson();
            Customer customer = g.fromJson(jsonData, Customer.class);

            boolean success = dao.insert(customer);

            if (!success || !dao.getLastError().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println(g.toJson("ERROR: " + dao.getLastError()));
            } else {
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.println(g.toJson(success));
            }
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
                Gson g = new Gson();
                out.println(g.toJson("ERROR: " + ex.getMessage()));
            }
        }
    }
}
