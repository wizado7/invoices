package impl;

import Interfaces.DAL.InvoiceDAO;
import config.ConnectionManager;
import entity.Invoice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class InvoiceDAOImpl implements InvoiceDAO {
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
    public Invoice getInvoiceById(int id) {
        String sql = "SELECT * FROM invoices WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setId(resultSet.getInt("id"));
                    invoice.setFirmId(resultSet.getInt("firm_id"));
                    invoice.setInvoiceDate(resultSet.getDate("invoice_date").toLocalDate());
                    return invoice;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public int getTotalInvoiceCount() throws SQLException {
        String query = "SELECT COUNT(*) AS total FROM invoices;";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
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
    public List<Invoice> getInvoicesByFirmName(String firmName) {
        List<Invoice> invoices = new ArrayList<>();
        String query = "SELECT i.id, i.firm_id, i.invoice_date, f.name AS firm_name " +
                "FROM invoices i " +
                "JOIN firms f ON i.firm_id = f.id " +
                "WHERE f.name LIKE ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, "%" + firmName + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setId((int) rs.getLong("id"));
                    invoice.setFirmId(rs.getInt("firm_id"));
                    invoice.setFirmName(rs.getString("firm_name"));
                    invoice.setInvoiceDate(rs.getDate("invoice_date").toLocalDate());
                    invoices.add(invoice);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    @Override
    public List<Invoice> getInvoicesByFirmNameAndDate(String firmName, String invoiceDate) {
        List<Invoice> invoices = new ArrayList<>();
        String query = """
        SELECT i.id, i.firm_id, i.invoice_date, f.name AS firm_name
        FROM invoices i
        JOIN firms f ON i.firm_id = f.id
        WHERE f.name LIKE ? AND DATE_FORMAT(i.invoice_date, '%Y-%m-%d') LIKE ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, "%" + firmName + "%");
            ps.setString(2, invoiceDate + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setId(rs.getInt("id"));
                    invoice.setFirmId(rs.getInt("firm_id"));
                    invoice.setFirmName(rs.getString("firm_name"));
                    invoice.setInvoiceDate(rs.getDate("invoice_date").toLocalDate());
                    invoices.add(invoice);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    @Override
    public List<Invoice> getInvoicesByDate(String invoiceDate) {
        List<Invoice> invoices = new ArrayList<>();
        String query = """
        SELECT i.id, i.firm_id, i.invoice_date, f.name AS firm_name 
        FROM invoices i 
        JOIN firms f ON i.firm_id = f.id 
        WHERE DATE_FORMAT(i.invoice_date, '%Y-%m-%d') LIKE ?
    """;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, invoiceDate + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setId(rs.getInt("id"));
                    invoice.setFirmId(rs.getInt("firm_id"));
                    invoice.setFirmName(rs.getString("firm_name"));
                    invoice.setInvoiceDate(rs.getDate("invoice_date").toLocalDate());
                    invoices.add(invoice);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    @Override
    public int getTotalSearchCount(String search) throws SQLException {
        return 0;
    }

    @Override
    public int getTotalCountByDateAndName(String search, Integer day, Integer month, Integer year) throws SQLException {
        return 0;
    }

    @Override
    public List<Invoice> getInvoicesWithPagination(int limit, int offset) throws SQLException {
        return null;
    }

    @Override
    public List<Invoice> searchInvoices(String firmName, String invoiceDate) {
        return null;
    }

    @Override
    public List<Invoice> searchInvoicesWithPagination(String search, int limit, int offset) throws SQLException {

        return null;
    }

    @Override
    public List<Invoice> searchInvoicesByDateAndName(String search, Integer day, Integer month, Integer year, int limit, int offset) throws SQLException {
        return null;
    }


}
