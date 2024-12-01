package servlets.invoice;

import Interfaces.DAL.InvoiceDAO;
import config.ConnectionManager;
import entity.Invoice;
import impl.InvoiceDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/invoices")
public class InvoicesListServlet extends HttpServlet {
    private InvoiceDAO invoiceDAO;

    @Override
    public void init() {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            invoiceDAO = new InvoiceDAOImpl(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageParam = req.getParameter("page");
        String searchParam = req.getParameter("search");
        // int currentPage = (pageParam != null) ? Integer.parseInt(pageParam) : 1;

        int currentPage = 1;

        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        int limit = 7;
        int offset = (currentPage - 1) * limit;

        try {
            List<Invoice> invoices;
            int totalInvoices = 0;

            if (searchParam != null && !searchParam.isEmpty()) {
                invoices = invoiceDAO.searchInvoicesWithPagination(searchParam, limit, offset);
            } else {
                invoices = invoiceDAO.getInvoicesWithPagination(limit, offset);
                totalInvoices = invoiceDAO.getTotalInvoiceCount();
            }

            int totalPages = (int) Math.ceil((double) totalInvoices / limit);

            req.setAttribute("invoices", invoices);
            req.setAttribute("currentPage", currentPage);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("searchParam", searchParam);
            req.getRequestDispatcher("/invoices.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при получении накладных");
        }
    }
}
