package Controller;

import DataAccess.PizzaSizeDAO;
import DataAccess.PizzaSizeDAO;
import Entity.PizzaSize;
import com.google.gson.Gson;
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

@WebServlet(name = "PizzaSizeService", urlPatterns = {"/PizzaSizeService/size", "/PizzaSizeService/size/*"})
public class PizzaSizeService extends HttpServlet {
    public PizzaSizeService() {
        System.out.println("PizzaSizeService constructor");
    }

    private PizzaSizeDAO getDAO(HttpServletRequest request) {
        HttpSession session = request.getSession();
        PizzaSizeDAO dao = (PizzaSizeDAO) session.getAttribute("pizzaSizeDAO");
        if (dao == null) {
            dao = new PizzaSizeDAO();
            session.setAttribute("pizzaSizeDAO", dao);
        }
        return dao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PizzaSizeDAO dao = getDAO(request);

        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            List<PizzaSize> t = dao.getAll();
            if (t.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println(gson.toJson("Error: " + dao.getLastError()));
            }
            else {
                System.out.println("controller.PizzaSizeService.doGet() n=" + t.size());
                out.println(gson.toJson(t));
            }
        }
    }
}
