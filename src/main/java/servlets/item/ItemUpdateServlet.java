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

@WebServlet("/items/update")
public class ItemUpdateServlet extends HttpServlet {

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
        int itemId;
        try {
            itemId = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException | NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный ID товара");
            return;
        }

        Item item = itemDAO.getItemById(itemId);
        if (item == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Товар с ID " + itemId + " не найден");
            return;
        }


        req.setAttribute("item", item);
        req.getRequestDispatcher("/items-update.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String idParam = req.getParameter("id");
            String name = req.getParameter("name");
            String priceParam = req.getParameter("price");

            System.out.println("Принятые параметры: id = " + idParam + ", name = " + name + ", price = " + priceParam);


            if (idParam == null || idParam.isEmpty()) {
                throw new IllegalArgumentException("ID пропал");
            }
            int itemId = Integer.parseInt(idParam);

            double price = Double.parseDouble(priceParam);

            Item item = new Item();
            item.setId(itemId);
            item.setName(name);
            item.setPrice(price);

            System.out.println("Обновленные товары " + item);


            try {
                itemDAO.updateItem(item);
            } catch (NumberFormatException e) {
                System.err.println("Неверный формат цифорок" + e.getMessage());
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат цифорок");


                resp.sendRedirect("/invoices");

            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверно введеные данные");
        }
    }
}


