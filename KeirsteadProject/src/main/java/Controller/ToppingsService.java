package Controller;

import DataAccess.EmployeeDAO;
import com.google.gson.Gson;
import DataAccess.ToppingsDAO;
import Entity.Toppings;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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

@WebServlet(name = "ToppingsService", urlPatterns = {"/ToppingsService/toppings", "/ToppingsService/toppings/*"})
public class ToppingsService extends HttpServlet {

    public ToppingsService() {
        System.out.println("ToppingsService constructor");
    }

    private ToppingsDAO getDAO(HttpServletRequest request) {
        HttpSession session = request.getSession();
        ToppingsDAO dao = (ToppingsDAO) session.getAttribute("toppingsDAO");
        if (dao == null) {
            dao = new ToppingsDAO();
            session.setAttribute("toppingsDAO", dao);
        }
        return dao;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        int username = (session != null) ? (int) session.getAttribute("username") : 1;

        ToppingsDAO dao = getDAO(request);

        try (PrintWriter out = response.getWriter()) {
            Scanner sc = new Scanner(request.getReader());
            String jsonData = sc.nextLine();

            Gson g = new Gson();
            Toppings t = g.fromJson(jsonData, Toppings.class);

            t.setEmpAdd(username);

            boolean success = dao.insert(t);

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

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ToppingsDAO dao = getDAO(request);
        try (PrintWriter out = response.getWriter()) {
            int id = Integer.parseInt(request.getPathInfo().substring(1));
            boolean success = dao.delete(new Toppings(id, "name", 1.00, Timestamp.valueOf(LocalDateTime.now()), 1, 1));
            Gson g = new Gson();
            if (!success || !dao.getLastError().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println(g.toJson("ERROR: " + dao.getLastError()));
            }
            else
                out.println(success);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ToppingsDAO dao = getDAO(request);

        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            List<Toppings> t = dao.getAll();
            if (t.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println(gson.toJson("Error: " + dao.getLastError()));
            }
            else {
                System.out.println("controller.ToppingsService.doGet() n=" + t.size());
                out.println(gson.toJson(t));
            }
        }
    }

}
