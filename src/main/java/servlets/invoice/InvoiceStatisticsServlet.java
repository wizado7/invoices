package servlets.invoice;


import Interfaces.DAL.FirmDAO;
import Interfaces.DAL.InvoiceDAO;
import config.ConnectionManager;
import entity.Invoice;
import impl.FirmDAOImpl;
import impl.InvoiceDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/invoices/statistics")
public class InvoiceStatisticsServlet extends HttpServlet {
    private InvoiceDAO invoiceDAO;
    private FirmDAO firmDAO;

    @Override
    public void init() {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            invoiceDAO = new InvoiceDAOImpl(connection);
            firmDAO = new FirmDAOImpl(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            List<Invoice> invoices = invoiceDAO.getAllInvoices();

            int totalInvoices = invoices.size();
            int totalFirms = firmDAO.getAllFirms().size();
            double totalAmount = invoices.stream().mapToDouble(Invoice::getTotalAmount).sum();


            req.setAttribute("totalInvoices", totalInvoices);
            req.setAttribute("totalFirms", totalFirms);
            req.setAttribute("totalAmount", totalAmount);

            req.getRequestDispatcher("/invoices-statistics.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при загрузке статистики");
        }
    }
}