package servlets.invoice;


import Interfaces.DAL.InvoiceDAO;
import config.ConnectionManager;
import entity.Firm;
import entity.Invoice;
import entity.InvoiceItem;
import entity.Item;
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
import java.util.Map;



@WebServlet("/invoices/update")
public class InvoiceUpdateServlet extends HttpServlet {
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

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
