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

@WebServlet("/invoices/search")
public class InvoiceSearchServlet extends HttpServlet {
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
        String dayParam = req.getParameter("day");
        String monthParam = req.getParameter("month");
        String yearParam = req.getParameter("year");

        int page = (pageParam != null) ? Integer.parseInt(pageParam) : 1;
        int limit = 7;
        int offset = (page - 1) * limit;

        Integer day = (dayParam != null && !dayParam.isEmpty()) ? Integer.parseInt(dayParam) : null;
        Integer month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam) : null;
        Integer year = (yearParam != null && !yearParam.isEmpty()) ? Integer.parseInt(yearParam) : null;

        try (Connection connection = ConnectionManager.getConnection()) {
            invoiceDAO = new InvoiceDAOImpl(connection);
            List<Invoice> invoices;
            int totalInvoices;

            if ((day != null || month != null || year != null) || (searchParam != null && !searchParam.isEmpty())) {
                invoices = invoiceDAO.searchInvoicesByDateAndName(searchParam, day, month, year, limit, offset);
                totalInvoices = invoiceDAO.getTotalCountByDateAndName(searchParam, day, month, year);
            } else {
                invoices = invoiceDAO.getInvoicesWithPagination(limit, offset);
                totalInvoices = invoiceDAO.getTotalInvoiceCount();
            }

            int totalPages = (int) Math.ceil((double) totalInvoices / limit);

            req.setAttribute("invoices", invoices);
            req.setAttribute("currentPage", page);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("searchParam", searchParam);
            req.setAttribute("day", day);
            req.setAttribute("month", month);
            req.setAttribute("year", year);

            req.getRequestDispatcher("/invoices.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при поиске накладных.");
        }
    }

}
