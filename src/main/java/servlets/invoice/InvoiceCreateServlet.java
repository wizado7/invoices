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
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/invoices/create")
public class InvoiceCreateServlet extends HttpServlet {
    private InvoiceDAO invoiceDAO;
    private ItemDAO itemDAO;
    private InvoiceItemDAO invoiceItemDAO;
    private FirmDAO firmDAO;


    @Override
    public void init() {
        Connection connection = null;
        try {
            connection = ConnectionManager.getConnection();
            invoiceDAO = new InvoiceDAOImpl(connection);
            itemDAO = new ItemDAOImpl(connection);
            invoiceItemDAO = new InvoiceItemDAOImpl(connection);
            firmDAO = new FirmDAOImpl(connection);
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
        try  {


            String firmName = req.getParameter("firmName");
            if (firmName == null || firmName.isBlank()) {
                req.setAttribute("error", "Имя фирмы не может быть пустым");
                req.getRequestDispatcher("/invoice-create.jsp").forward(req, resp);
                return;
            }

            Firm firm = firmDAO.getFirmByName(firmName);
            if (firm == null) {
                req.setAttribute("error", "Фирма с таким названием не существует.");
                req.setAttribute("firmName", firmName);
                req.setAttribute("items", itemDAO.getAllItems());
                req.getRequestDispatcher("/invoice-create.jsp").forward(req, resp);
                return;
            }

            Invoice invoice = new Invoice();
            invoice.setFirmId(firm.getId());
            invoice.setInvoiceDate(LocalDate.now());
            invoiceDAO.addInvoice(invoice);


            for (Item item : itemDAO.getAllItems()) {
                String quantityStr = req.getParameter("item_" + item.getId());
                if (quantityStr != null && !quantityStr.isBlank()) {
                    int quantity = Integer.parseInt(quantityStr);
                    if (quantity > 0) {
                        InvoiceItem invoiceItem = new InvoiceItem();
                        invoiceItem.setInvoiceId(invoice.getId());
                        invoiceItem.setItemId(item.getId());
                        invoiceItem.setQuantity(quantity);
                        invoiceItemDAO.addInvoiceItem(invoiceItem);
                    }
                }
            }
            resp.sendRedirect("/invoices");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка при создании накладной");
        }
    }
}
