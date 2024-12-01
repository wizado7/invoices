package impl;

import Interfaces.DAL.InvoiceDAO;
import entity.Invoice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAOImpl  implements InvoiceDAO {

    private final Connection connection;

    public InvoiceDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addInvoice(Invoice invoice) {
        String sql = "INSERT INTO invoices (firm_id, invoice_date) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, invoice.getFirmId());
            stmt.setDate(2, java.sql.Date.valueOf(invoice.getInvoiceDate()));
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    invoice.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateInvoice(Invoice invoice) {
        String sql = "UPDATE invoices SET firm_id = ?, invoice_date = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, invoice.getFirmId());
            statement.setDate(2, Date.valueOf(invoice.getInvoiceDate()));
            statement.setInt(3, invoice.getId());

            int rowsUpdated = statement.executeUpdate();
            System.out.println("Накладные обновлены " + rowsUpdated + " строки");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteInvoice(long id) {
        String sql = "DELETE FROM invoices WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = """
        SELECT 
            invoices.id AS invoice_id,
            invoices.firm_id,
            invoices.invoice_date,
            firms.name AS firm_name,  
            (SELECT 
                COALESCE(SUM(invoice_items.quantity * items.price), 0) 
             FROM 
                invoice_items 
             JOIN 
                items ON invoice_items.item_id = items.id 
             WHERE 
                invoice_items.invoice_id = invoices.id) AS total_amount
        FROM invoices
        JOIN firms ON invoices.firm_id = firms.id;
        """;

        System.out.println("Executing SQL: " + sql);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setId(rs.getInt("invoice_id"));
                invoice.setFirmId(rs.getInt("firm_id"));
                invoice.setInvoiceDate(rs.getDate("invoice_date").toLocalDate());
                invoice.setFirmName(rs.getString("firm_name"));
                invoice.setTotalAmount(rs.getDouble("total_amount"));
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    @Override
    public Invoice getInvoiceById(int id) {
        return null;
    }

    @Override
    public List<Invoice> searchInvoices(String firmName, String invoiceDate) {
        return null;
    }

    @Override
    public List<Invoice> getInvoicesWithPagination(int limit, int offset) throws SQLException {
        return null;
    }

    @Override
    public int getTotalInvoiceCount() throws SQLException {
        return 0;
    }

    @Override
    public List<Invoice> getInvoicesByFirmName(String firmName) {
        return null;
    }

    @Override
    public List<Invoice> getInvoicesByFirmNameAndDate(String firmName, String invoiceDate) {
        return null;
    }

    @Override
    public List<Invoice> getInvoicesByDate(String invoiceDate) {
        return null;
    }

    @Override
    public int getTotalSearchCount(String search) throws SQLException {
        return 0;
    }

    @Override
    public List<Invoice> searchInvoicesWithPagination(String search, int limit, int offset) throws SQLException {
        return null;
    }

    @Override
    public List<Invoice> searchInvoicesByDateAndName(String search, Integer day, Integer month, Integer year, int limit, int offset) throws SQLException {
        return null;
    }

    @Override
    public int getTotalCountByDateAndName(String search, Integer day, Integer month, Integer year) throws SQLException {
        return 0;
    }
}
