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
        String query = """
                    SELECT COUNT(DISTINCT invoices.id) AS total
        FROM invoices
        LEFT JOIN firms ON invoices.firm_id = firms.id
        WHERE firms.name LIKE ? OR invoices.id LIKE ?;
    """;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + search + "%");
            stmt.setString(2, "%" + search + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
                return 0;
            }
        }
    }

    @Override
    public int getTotalCountByDateAndName(String search, Integer day, Integer month, Integer year) throws SQLException {
        StringBuilder query = new StringBuilder("SELECT COUNT(DISTINCT invoices.id) AS total FROM invoices LEFT JOIN firms ON invoices.firm_id = firms.id WHERE 1=1");

        if (search != null && !search.isEmpty()) {
            query.append(" AND (firms.name LIKE ? OR invoices.id LIKE ?)");
        }
        if (day != null) {
            query.append(" AND DAY(invoices.invoice_date) = ?");
        }
        if (month != null) {
            query.append(" AND MONTH(invoices.invoice_date) = ?");
        }
        if (year != null) {
            query.append(" AND YEAR(invoices.invoice_date) = ?");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            int paramIndex = 1;
            if (search != null && !search.isEmpty()) {
                stmt.setString(paramIndex++, "%" + search + "%");
                stmt.setString(paramIndex++, "%" + search + "%");
            }
            if (day != null) {
                stmt.setInt(paramIndex++, day);
            }
            if (month != null) {
                stmt.setInt(paramIndex++, month);
            }
            if (year != null) {
                stmt.setInt(paramIndex++, year);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt("total") : 0;
            }
        }
    }

    @Override
    public List<Invoice> getInvoicesWithPagination(int limit, int offset) throws SQLException {
        String query = """
        SELECT invoices.id AS invoice_id, firms.name AS firm_name, invoices.invoice_date,
               COALESCE(SUM(invoice_items.quantity * items.price), 0) AS total_amount
        FROM invoices
        LEFT JOIN firms ON invoices.firm_id = firms.id
        LEFT JOIN invoice_items ON invoices.id = invoice_items.invoice_id
        LEFT JOIN items ON invoice_items.item_id = items.id
        GROUP BY invoices.id, firms.name, invoices.invoice_date
        LIMIT ? OFFSET ?;
    """;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Invoice> invoices = new ArrayList<>();
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setId(rs.getInt("invoice_id"));
                    invoice.setFirmName(rs.getString("firm_name"));
                    invoice.setInvoiceDate(rs.getDate("invoice_date").toLocalDate());
                    invoice.setTotalAmount(rs.getDouble("total_amount"));
                    invoices.add(invoice);
                }
                return invoices;
            }
        }
    }

    @Override
    public List<Invoice> searchInvoices(String firmName, String invoiceDate) {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT i.id, i.invoice_date, f.firm_name " +
                "FROM invoices i " +
                "JOIN firms f ON i.firm_id = f.id WHERE 1=1";

        // Добавляем условия поиска
        if (firmName != null && !firmName.isEmpty()) {
            sql += " AND f.firm_name LIKE ?";
        }
        if (invoiceDate != null && !invoiceDate.isEmpty()) {
            sql += " AND i.invoice_date = ?";
        }

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int index = 1;
            if (firmName != null && !firmName.isEmpty()) {
                stmt.setString(index++, "%" + firmName + "%");
            }

            if (invoiceDate != null && !invoiceDate.isEmpty()) {
                stmt.setString(index, invoiceDate);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setId(rs.getInt("id"));
                invoice.setInvoiceDate(rs.getDate("invoice_date").toLocalDate());
                invoice.setFirmName(rs.getString("firm_name"));  // Устанавливаем название фирмы
                invoices.add(invoice);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }
    @Override
    public List<Invoice> searchInvoicesWithPagination(String search, int limit, int offset) throws SQLException {
        String query = """
        SELECT invoices.id AS invoice_id, firms.name AS firm_name, invoices.invoice_date,
               COALESCE(SUM(invoice_items.quantity * items.price), 0) AS total_amount
        FROM invoices
        LEFT JOIN firms ON invoices.firm_id = firms.id
        LEFT JOIN invoice_items ON invoices.id = invoice_items.invoice_id
        LEFT JOIN items ON invoice_items.item_id = items.id
        WHERE firms.name LIKE ? OR invoices.id LIKE ?
        GROUP BY invoices.id, firms.name, invoices.invoice_date
        LIMIT ? OFFSET ?;
    """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + search + "%");
            stmt.setString(2, "%" + search + "%");
            stmt.setInt(3, limit);
            stmt.setInt(4, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Invoice> invoices = new ArrayList<>();
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setId(rs.getInt("invoice_id"));
                    invoice.setFirmName(rs.getString("firm_name"));
                    invoice.setInvoiceDate(rs.getDate("invoice_date").toLocalDate());
                    invoice.setTotalAmount(rs.getDouble("total_amount"));
                    invoices.add(invoice);
                }
                return invoices;
            }
        }
    }

    @Override
    public List<Invoice> searchInvoicesByDateAndName(String search, Integer day, Integer month, Integer year, int limit, int offset) throws SQLException {
        StringBuilder query = new StringBuilder("""
        SELECT invoices.id AS invoice_id, firms.name AS firm_name, invoices.invoice_date,
               COALESCE(SUM(invoice_items.quantity * items.price), 0) AS total_amount
        FROM invoices
        LEFT JOIN firms ON invoices.firm_id = firms.id
        LEFT JOIN invoice_items ON invoices.id = invoice_items.invoice_id
        LEFT JOIN items ON invoice_items.item_id = items.id
        WHERE 1=1
    """);

        if (search != null && !search.isEmpty()) {
            query.append(" AND (firms.name LIKE ? OR invoices.id LIKE ?)");
        }
        if (day != null) {
            query.append(" AND DAY(invoices.invoice_date) = ?");
        }
        if (month != null) {
            query.append(" AND MONTH(invoices.invoice_date) = ?");
        }
        if (year != null) {
            query.append(" AND YEAR(invoices.invoice_date) = ?");
        }

        query.append(" GROUP BY invoices.id, firms.name, invoices.invoice_date LIMIT ? OFFSET ?");

        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            int paramIndex = 1;
            if (search != null && !search.isEmpty()) {
                stmt.setString(paramIndex++, "%" + search + "%");
                stmt.setString(paramIndex++, "%" + search + "%");
            }
            if (day != null) {
                stmt.setInt(paramIndex++, day);
            }
            if (month != null) {
                stmt.setInt(paramIndex++, month);
            }
            if (year != null) {
                stmt.setInt(paramIndex++, year);
            }
            stmt.setInt(paramIndex++, limit);
            stmt.setInt(paramIndex, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Invoice> invoices = new ArrayList<>();
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setId(rs.getInt("invoice_id"));
                    invoice.setFirmName(rs.getString("firm_name"));
                    invoice.setInvoiceDate(rs.getDate("invoice_date").toLocalDate());
                    invoice.setTotalAmount(rs.getDouble("total_amount"));
                    invoices.add(invoice);
                }
                return invoices;
            }
        }
    }


}
