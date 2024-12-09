package Controller;

import DataAccess.PizzaCrustDAO;
import DataAccess.ToppingsDAO;
import Entity.Toppings;
import com.google.gson.Gson;
import Entity.PizzaCrust;
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

@WebServlet(name = "PizzaCrustService", urlPatterns = {"/PizzaCrustService/crusts", "/PizzaCrustService/crusts/*"})
public class PizzaCrustService extends HttpServlet {

    public PizzaCrustService() {
        System.out.println("PizzaCrustService constructor");
    }

    private PizzaCrustDAO getDAO(HttpServletRequest request) {
        HttpSession session = request.getSession();
        PizzaCrustDAO dao = (PizzaCrustDAO) session.getAttribute("pizzaCrustDAO");
        if (dao == null) {
            dao = new PizzaCrustDAO();
            session.setAttribute("pizzaCrustDAO", dao);
        }
        return dao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PizzaCrustDAO dao = getDAO(request);

        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            List<PizzaCrust> t = dao.getAll();
            if (t.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println(gson.toJson("Error: " + dao.getLastError()));
            }
            else {
                System.out.println("controller.PizzaCrustService.doGet() n=" + t.size());
                out.println(gson.toJson(t));
            }
        }
    }
}
