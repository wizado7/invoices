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

@WebServlet("/items/create")
public class ItemCreateServlet extends HttpServlet {

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/items-create.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String itemName = request.getParameter("itemName");
            String itemPriceStr = request.getParameter("itemPrice");

            if (itemName == null || itemPriceStr == null || itemName.isEmpty() || itemPriceStr.isEmpty()) {
                request.setAttribute("error", "Все поля обязательны для заполнения.");
                request.getRequestDispatcher("/items-create.jsp").forward(request, response);
                return;
            }

            double itemPrice = Double.parseDouble(itemPriceStr);


            Item newItem = new Item();
            newItem.setName(itemName);
            newItem.setPrice(itemPrice);


            itemDAO.addItem(newItem);

            response.sendRedirect( "/invoices");
        } catch (Exception e) {
            throw new ServletException("Ошибка при добавлении товара", e);
        }
    }
}
