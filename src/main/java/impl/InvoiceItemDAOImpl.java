package impl;

import Interfaces.DAL.InvoiceItemDAO;
import entity.InvoiceItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceItemDAOImpl implements InvoiceItemDAO {
    private final Connection connection;

    public InvoiceItemDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addInvoiceItem(InvoiceItem invoiceItem) {
        String sql = "INSERT INTO invoice_items (invoice_id, item_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, invoiceItem.getInvoiceId());
            stmt.setLong(2, invoiceItem.getItemId());
            stmt.setInt(3, invoiceItem.getQuantity());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                invoiceItem.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteInvoiceItemsByInvoiceId(int invoiceId) {
        String sql = "DELETE FROM invoice_items WHERE invoice_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, invoiceId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateQuantity(int invoiceId, int itemId, int quantity) {
        String sql = "UPDATE invoice_items SET quantity = ? WHERE invoice_id = ? AND item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, invoiceId);
            stmt.setInt(3, itemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateInvoiceItem(InvoiceItem invoiceItem) {
        String sql = "UPDATE invoice_items SET quantity = ? WHERE invoice_id = ? AND item_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, invoiceItem.getQuantity());
            statement.setInt(2, invoiceItem.getInvoiceId());
            statement.setInt(3, invoiceItem.getItemId());

            int rowsUpdated = statement.executeUpdate();
            System.out.println("InvoiceItem обновлен: " + rowsUpdated + " строки");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteInvoiceItem(long id) {
        String sql = "DELETE FROM invoice_items WHERE invoice_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteInvoiceItem(int invoiceId, int itemId) throws SQLException {
        String sql = "DELETE FROM invoice_items WHERE invoice_id = ? AND item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, invoiceId);
            stmt.setInt(2, itemId);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<InvoiceItem> getInvoiceItemsByInvoiceId(int invoiceId) {
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        String sql = "SELECT * FROM invoice_items WHERE invoice_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, invoiceId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                InvoiceItem invoiceItem = new InvoiceItem();
                invoiceItem.setInvoiceId(rs.getInt("invoice_id"));
                invoiceItem.setItemId(rs.getInt("item_id"));
                invoiceItem.setQuantity(rs.getInt("quantity"));
                invoiceItems.add(invoiceItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoiceItems;
    }


    @Override
    public InvoiceItem getInvoiceItemByInvoiceAndItemId(int invoiceId, int itemId) {
        String sql = "SELECT * FROM invoice_items WHERE invoice_id = ? AND item_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, invoiceId);
            statement.setInt(2, itemId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    InvoiceItem invoiceItem = new InvoiceItem();
                    invoiceItem.setId(resultSet.getInt("id"));
                    invoiceItem.setInvoiceId(resultSet.getInt("invoice_id"));
                    invoiceItem.setItemId(resultSet.getInt("item_id"));
                    invoiceItem.setQuantity(resultSet.getInt("quantity"));
                    return invoiceItem;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
