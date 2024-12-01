package servlets.invoice;

import Interfaces.DAL.InvoiceDAO;
import Interfaces.DAL.InvoiceItemDAO;
import config.ConnectionManager;
import impl.InvoiceDAOImpl;
import impl.InvoiceItemDAOImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


@WebServlet("/invoices/delete")
public class InvoiceDeleteServlet extends HttpServlet {
    private InvoiceDAO invoiceDAO;
    private InvoiceItemDAO invoiceItemDAO;

    @Override
    public void init() {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            invoiceDAO = new InvoiceDAOImpl(connection);
            invoiceItemDAO = new InvoiceItemDAOImpl(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (Connection connection = ConnectionManager.getConnection()) {
            long id = Long.parseLong(req.getParameter("id"));
            invoiceItemDAO.deleteInvoiceItem(id);
            invoiceDAO.deleteInvoice(id);

            String currentPage = req.getParameter("page");
            String searchParam = req.getParameter("search");

            resp.sendRedirect("/invoices?page=" + currentPage + "&search=" + searchParam);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка удаления накладной");
        }
    }
}


