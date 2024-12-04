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

@WebServlet("/items/search")
public class ItemSearchServlet extends HttpServlet {
    private ItemDAO itemDAO;

    @Override
    public void init() {

        try {
            Connection connection = ConnectionManager.getConnection();
            itemDAO = new ItemDAOImpl(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String itemName = request.getParameter("itemName");
        String itemPriceStr = request.getParameter("itemPrice");

        List<Item> items;

        if (itemName != null && !itemName.isEmpty()) {
            items = itemDAO.getItemsByName(itemName);
        } else if (itemPriceStr != null && !itemPriceStr.isEmpty()) {
            try {
                double itemPrice = Double.parseDouble(itemPriceStr);
                items = itemDAO.getItemsByPrice(itemPrice);
            } catch (NumberFormatException e) {
                items = null;
            }
        } else {
            try {
                items = itemDAO.getAllItems();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        request.setAttribute("items", items);
        request.getRequestDispatcher("/items.jsp").forward(request, response);
    }

}
