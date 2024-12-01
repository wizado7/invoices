package servlets.invoice;

import Interfaces.DAL.InvoiceDAO;
import Interfaces.DAL.ItemDAO;
import config.ConnectionManager;
import entity.Firm;
import entity.Invoice;
import entity.InvoiceItem;
import entity.Item;
import impl.InvoiceDAOImpl;
import impl.ItemDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/invoices/create")
public class InvoiceCreateServlet extends HttpServlet {
    private InvoiceDAO invoiceDAO;
    private ItemDAO itemDAO;

    @Override
    public void init() {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            invoiceDAO = new InvoiceDAOImpl(connection);
            itemDAO = new ItemDAOImpl(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Item> items = itemDAO.getAllItems();

            req.setAttribute("items", items);
            req.getRequestDispatcher("/invoice-create.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при загрузке страницы создания накладной");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
