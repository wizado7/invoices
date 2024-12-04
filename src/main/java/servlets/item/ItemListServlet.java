package servlets.item;


import Interfaces.DAL.ItemDAO;
import config.ConnectionManager;
import entity.Item;
import impl.ItemDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;



@WebServlet("/items")
public class ItemListServlet extends HttpServlet {

    private ItemDAO itemDAO;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = ConnectionManager.getConnection();
            this.itemDAO = new ItemDAOImpl(connection);
        } catch (SQLException e) {
            throw new ServletException("Ошибка подключения к базе данных", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Item> items = itemDAO.getAllItems();
            req.setAttribute("items", items);

            req.getRequestDispatcher("/items.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при загрузке накладных");
        }
    }
}
