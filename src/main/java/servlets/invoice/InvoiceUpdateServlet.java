package servlets.invoice;


import Interfaces.DAL.FirmDAO;
import Interfaces.DAL.InvoiceDAO;
import Interfaces.DAL.InvoiceItemDAO;
import Interfaces.DAL.ItemDAO;
import config.ConnectionManager;
import entity.Firm;
import entity.Invoice;
import entity.InvoiceItem;
import entity.Item;
import impl.FirmDAOImpl;
import impl.InvoiceDAOImpl;
import impl.InvoiceItemDAOImpl;
import impl.ItemDAOImpl;
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
import java.util.Map;



@WebServlet("/invoices/update")
public class InvoiceUpdateServlet extends HttpServlet {
    private InvoiceDAO invoiceDAO;
    private ItemDAO itemDAO;
    private FirmDAO firmDAO;
    private InvoiceItemDAO invoiceItemDAO;

    @Override
    public void init() {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            invoiceDAO = new InvoiceDAOImpl(connection);
            itemDAO = new ItemDAOImpl(connection);
            firmDAO = new FirmDAOImpl(connection);
            invoiceItemDAO = new InvoiceItemDAOImpl(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int invoiceId = Integer.parseInt(req.getParameter("id"));
            try {
                Invoice invoice = invoiceDAO.getInvoiceById(invoiceId);
                if (invoice == null) {
                    throw new IllegalArgumentException("Накладная с указанным ID не найдена");
                }

                List<InvoiceItem> invoiceItems = invoiceItemDAO.getInvoiceItemsByInvoiceId(invoiceId);
                List<Item> items = itemDAO.getAllItems();
                Firm firm = firmDAO.getFirmById(invoice.getFirmId());

                req.setAttribute("invoice", invoice);
                req.setAttribute("invoiceItems", invoiceItems);
                req.setAttribute("items", items);
                req.setAttribute("firm", firm);

                req.setAttribute("currentPage", req.getParameter("page"));
                req.setAttribute("searchParam", req.getParameter("search"));
                req.setAttribute("day", req.getParameter("day"));
                req.setAttribute("month", req.getParameter("month"));
                req.setAttribute("year", req.getParameter("year"));

                req.getRequestDispatcher("/invoice-update.jsp").forward(req, resp);
            } catch (IllegalArgumentException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при загрузке страницы редактирования накладной");
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try  {
            int invoiceId = Integer.parseInt(req.getParameter("invoiceId"));
            String firmName = req.getParameter("firmName");

            Firm firm = firmDAO.getFirmByName(firmName);
            if (firm == null) {
                firm = new Firm();
                firm.setName(firmName);
                firmDAO.addFirm(firm);
                firm = firmDAO.getFirmByName(firmName);
            }


            Invoice invoice = invoiceDAO.getInvoiceById(invoiceId);
            invoice.setFirmId(firm.getId());
            invoiceDAO.updateInvoice(invoice);

            Map<String, String[]> parameterMap = req.getParameterMap();
            for (String paramName : parameterMap.keySet()) {
                if (paramName.startsWith("item_")) {
                    int itemId = Integer.parseInt(paramName.substring(5));
                    int quantity = Integer.parseInt(req.getParameter(paramName));

                    if (quantity > 0) {
                        InvoiceItem invoiceItem = invoiceItemDAO.getInvoiceItemByInvoiceAndItemId(invoiceId, itemId);
                        if (invoiceItem == null) {

                            invoiceItem = new InvoiceItem();
                            invoiceItem.setInvoiceId(invoiceId);
                            invoiceItem.setItemId(itemId);
                            invoiceItem.setQuantity(quantity);
                            invoiceItemDAO.addInvoiceItem(invoiceItem);
                        } else {
                            invoiceItem.setQuantity(quantity);
                            invoiceItemDAO.updateInvoiceItem(invoiceItem);
                        }
                    } else {
                        invoiceItemDAO.deleteInvoiceItem(invoiceId, itemId);
                    }
                }
            }

            String currentPage = req.getParameter("page");
            String searchParam = req.getParameter("search");
            String day = req.getParameter("day");
            String month = req.getParameter("month");
            String year = req.getParameter("year");

            resp.sendRedirect("/invoices?page=" + currentPage + "&search=" + searchParam + "&day=" + day + "&month=" + month + "&year=" + year);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при обновлении накладной");
        }
    }
}
