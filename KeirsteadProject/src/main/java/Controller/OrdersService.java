package Controller;

import DataAccess.ToppingsDAO;
import Entity.Toppings;
import com.google.gson.Gson;
import DataAccess.OrdersDAO;
import Entity.Orders;
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

@WebServlet(name = "OrdersService", urlPatterns = {"/OrdersService/orders", "/OrdersService/orders/*"})
public class OrdersService extends HttpServlet {
    public OrdersService() {
        System.out.println("OrdersService constructor");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        OrdersDAO dao = getDAO(request);

        try (PrintWriter out = response.getWriter()) {
            int id = Integer.parseInt(request.getPathInfo().substring(1));
            Gson g = new Gson();

            boolean success = dao.update(new Orders(id, 1, 1.0, 1.0, 1.0,
                    "", Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now())));

            if (!success || !dao.getLastError().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println(g.toJson("ERROR: " + dao.getLastError()));
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
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

    private OrdersDAO getDAO(HttpServletRequest request) {
        HttpSession session = request.getSession();
        OrdersDAO dao = (OrdersDAO) session.getAttribute("ordersDAO");
        if (dao == null) {
            dao = new OrdersDAO();
            session.setAttribute("ordersDAO", dao);
        }
        return dao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrdersDAO dao = getDAO(request);

        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            List<Orders> o = dao.getAll();
            if (o.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println(gson.toJson("Error: " + dao.getLastError()));
            }
            else {
                System.out.println("controller.ToppingsService.doGet() n=" + o.size());
                out.println(gson.toJson(o));
            }
        }
    }
}
